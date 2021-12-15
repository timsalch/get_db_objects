#/usr/bin/env python

import snowflake.connector
import ast
import csv
import json
import sys
import queue

#open configuration file
#need to put the location of the config file here

#get the environment form the command line
environment_param = sys.argv[1].upper()

#pulling this from the inventory file now
#project_param = sys.argv[2].upper()

inventory_file = 'DevOpsHelpers/snowflake_inventory_prod.txt'

'''
release_dir = './'
config_file = open(release_dir + 'release.config').read()
config_dict = ast.literal_eval(config_file)
'''

#just a finction to save typing time
def print_label(label):
    print('\n\n###########################')
    print(label)
    print('###########################\n')

def parse_inventory(inventory):
    upgrade_files = []
    with open(inventory, newline='') as csvfile:
        inventory_reader = csv.DictReader(csvfile, delimiter='\t')
        for row in inventory_reader:
            #print(row['Localpath'])
            upgrade_files.append(row)
    return upgrade_files

'''
This was for the JSON version of the config file which we are not using.  Leaving the code for now.  
def parse_objects(release_configuration):
    for db in (release_configuration['objects']['databases']):
        db_name = db['name']
        print('Database: ' + db_name)
        for schema in (db['schemas']):
            schema_name = schema['name']
            print(schema_name)
            for object_type in (schema['objects'].keys()):
                print(object_type)
                for obj in schema['objects'][object_type]:
                    #print(obj)
                    obj_name = obj
                    full_name = db_name + '.' + schema_name + '.' + obj_name
                    print('  ' + full_name)

#parse and print the components of rht econfiguration file
print_label('functional_area')
print(config_dict['functional_area'])
func_area = config_dict['functional_area']


print_label('Project Name')
print(config_dict['project_name'])
project_name = config_dict['project_name']


print_label('Objects Included in release')

parse_objects(config_dict)

objects_to_deploy = config_dict['objects']


print_label('Test Plan for release')
for test_type in (config_dict['test_plan']):
    for test in (config_dict['test_plan'][test_type]):
        print(test_type + ': ' + test)
test_plan = config_dict['test_plan']
#print(test_plan)
'''
print('\n')

#connect to snowflake
'''
SCE account
print('Conencting to Snowflake...')
ctx = snowflake.connector.connect(
    user='EDWSF_DEVOPS_SVC',
    password='',
    account='edwsce.west-us-2.azure',
    authenticator='https://eix.okta.com',
    role='sysadmin',
    warehouse='admin_wh'
)
'''

#My account
print('Conencting to Snowflake...')
if environment_param.startswith('PROD'):
    role_name = 'EDWSF_DEVOPS_SVC_PROD_ROLE'
else:
    role_name = 'EDWSF_DEVOPS_PT_PT_ROLE'
ctx = snowflake.connector.connect(
    user='salchta',
    #password='u[hJqYfbcCQ{68nvZMcF',
    authenticator='externalbrowser',
    account='edwsce.west-us-2.azure',
    role=role_name,
    warehouse='admin_wh'
)

#function to simplify code
def provision_environment(upgrade_file, environment_name): #, project, functional_area, test_environment):
    project_name = upgrade_file[0]['project']
    functional_area = upgrade_file[0]['functional_area']
    cs = ctx.cursor()
    try:
        cs.execute("call admin_db.devops.sp_provision_environment('{}', '{}', '{}')".format(project_name, functional_area, environment_name))
        one_row = cs.fetchone()
        print('Test Env Config: \n')
        #print(one_row[0])
    finally:
        cs.close()
    return one_row[0]

def upgrade_environment(upgrade_file, objects, environment):
    object_dict = json.loads(objects)
    cs=ctx.cursor()
    #create a queue for failed items
    failed_items_queue = queue.Queue()
    #dictionary to keep track of how many times each cmd has failed
    failed_objects = {}
    for db_obj in upgrade_file:
        if (environment.upper().startswith('PROD')) and (db_obj['Snowflake_Db'].upper() == object_dict['prod_database_to_clone'].upper() ):
            #inventory file actually has source database, not destination
            #fixed i think
            db_name = object_dict['prod_database_to_clone'].upper()
        elif (environment.upper().startswith('PROD')) and (db_obj['Snowflake_Db'].upper() == object_dict['jobmetadata_to_clone'].upper() ):
            #inventory actually has soruce database, not desintation
            #fixed i think
            db_name = object_dict['jobmetadata_to_clone'].upper()
        elif (db_obj['Snowflake_Db'].upper() == object_dict['test_jobmetadata_to_create'].upper()):
            db_name = object_dict['test_jobmetadata_to_create'].upper()
        elif (db_obj['Snowflake_Db'].upper() == object_dict['test_database_to_create'].upper() ):
            db_name = object_dict['test_database_to_create'].upper()
        else:
            print("!!!! inventory obejct does not match deployment database !!!!!")
            exit()
        schema_name = db_obj['Snowflake_Schema'].upper()
        print('upgrading: ' + db_name + ' with ' + db_obj['path'])
        upgrade_file = db_obj['path']
        use_cmd = "use {}.{}".format(db_name, schema_name)
        cs.execute(use_cmd)
        sqlcmd = open(upgrade_file).read()
        #us execute string to execute multiple commands in the file
        #cs.execute(sqlcmd)
        #since order matters items may fail
        #if they do fail, put it in a queue to reprocess at the end
        
        try:
            ctx.execute_string(sqlcmd)
        except:
            print(db_obj['path'] + ' failed, will try again up to max 3 times')
            cmd = use_cmd + '; ' + sqlcmd 
            failed_items_queue.put(cmd)
            failed_objects[cmd] = 1

    #once done, empty the queue
    while ((not failed_items_queue.empty()) & (max(failed_objects.values()) <= 3) ):
        #put these in a try catch block
        #only want to retry the same obejct 3 times to rpevent loop
        try:
            cmd_string = failed_items_queue.get()
            print('\nRe-executing: ' + cmd_string[0:200])
            ctx.execute_string(cmd_string)
            print('Success!')
        except:
            failed_objects[cmd_string] = failed_objects[cmd_string] + 1
            failed_items_queue.put(cmd_string)
            print('Failed ' + str(failed_objects[cmd_string]) + " times: " )
    
    if ((max(failed_objects.values()) > 3)):
        print('!!!!!!!!  DEPLOYMENT FAILED: RETRY LIMIT EXCEEDED !!!!!!!!!')
                



def enforce_standards(database):
    cs = ctx.cursor()
    try:
        cs.execute("call devops_db.framework.sp_enforce_standards('{}')".format(database))
        one_row = cs.fetchone()
        print('Result: ' + one_row[0])
    finally:
        cs.close()

def execute_tests(test):
    test_procedure_cmd = "call devops_db.framework.sp_{}()".format(test)
    cs = ctx.cursor()
    try:
        cs.execute(test_procedure_cmd)
        one_row = cs.fetchone()
        print('Result: ' + one_row[0])
    finally:
        cs.close()

'''
provision test environment

    whats needed
        core testing from devopsdb - put in proc as its the same for every test
        functional area - give as parameter to procedure which will grab the prod databases for this functional area
        sp_provision_environment(functional_area, project_name, environment_to_provision)
            always clones job_metadata
            clones all databases for the functional area
            new objects are prefixed with project & environment
            

'''
#provsion the environment

print('\nProvisioning Environment\n')

#pulling these fromt eh inventory file
#test_env = 'QA'
#project_name = 'EDC'
#func_area = 'BUSINESS'

upgrade_list = parse_inventory(inventory_file)

#provision the environment once
print('\nprovisioning environment for ' + environment_param)
env_objects = provision_environment(upgrade_list, environment_param) #, project_param, func_area, environment_param)
print(env_objects)
print('\nPromoting changes...\n')
#loop through the objects
#extract the script for that object in github
#apply the script to snowflake

print('\nUpgrading the following objects...\n')
upgrade_list = parse_inventory(inventory_file)
upgrade_environment(upgrade_list, env_objects, environment_param)

#rule enforcement goes here - Naming
print('\nChecking for Standards...\n')

print('Not Implemented')
#enforce_standards(test_env)
#naming conventions
#rbac
#check if there is an objects committed in git which was not applied in snowflake

'''
this was for doing test, not applicable

for test_env in (test_plan):
    #provision the environment once
    print('\nprovisioning environment for ' + test_env)
    provision_environment(project_name, func_area, test_env)
    print('\nUpgrading environment from dev...\n')
    #loop through the objects
    #extract the script for that object in github
    #apply the script to snowflake
    print('\nUpgrading the following objects...\n')
    parse_inventory(inventory_file)
    #parse_objects(config_dict)
    
    #rule enforcement goes here - Naming
    print('\nChecking for Standards...\n')
    enforce_standards(test_env)
    #naming conventions
    #rbac
    #check if there is an objects committed in git which was not applied in snowflake


    #execute the tests
    for test in (test_plan[test_env]):    

        print('\nExecuting test: ' + test_env + ':' + test)

        
        #execute each the test
        execute_tests(test)
    
    print('\nDestroying environment: ' + test_env)


'''
print('\nOBJECT PROMOTION COMPLETE\n')
#apply upgrade scripts
#execute the test


#apply upgade scripts from github

'''
#execute tests
always: rule checks (naming, etc)
'''


'''
#provsion the environment
cs = ctx.cursor()
try:
    cs.execute("call devops_db.framework.sp_provision_environment('my_environment')")
    one_row = cs.fetchone()
    print(one_row[0])
finally:
    cs.close()

cs = ctx.cursor()
try:
    cs.execute("call devops_db.framework.sp_db_to_db_regression_test()")
    one_row = cs.fetchone()
    print(one_row[0])
finally:
    cs.close()
ctx.close()
'''

if(ctx):
    ctx.close
