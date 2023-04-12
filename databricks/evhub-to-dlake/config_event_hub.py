# Databricks notebook source
# MAGIC %md
# MAGIC ## Event Hub: namespace, topic, and consumer group - from Azure
# MAGIC 
# MAGIC #### event_hub_namespace
# MAGIC #### event_hub_topic
# MAGIC 
# MAGIC ## Event Hub: key and value - from Azure event hub policy, stored in secrets: e.g. cluster config 
# MAGIC 
# MAGIC #### event_hub_sas_key_name
# MAGIC #### event_hub_sas_key_value
# MAGIC #### event_hub_topic_consumer_group
# MAGIC ---
# MAGIC 
# MAGIC #### event_hub_conn_str

# COMMAND ----------


event_hub_namespace = "todo_namespace"
event_hub_topic = "todo_topic"

event_hub_sas_key_name = "todo_key_name"
event_hub_sas_key_value = "todo_key_value"

event_hub_topic_consumer_group = "todo_consumer_grp"

# COMMAND ----------


event_hub_conn_str = "Endpoint=sb://{0}.servicebus.windows.net/;EntityPath={1};SharedAccessKeyName={2};SharedAccessKey={3}".format(event_hub_namespace, event_hub_topic, event_hub_sas_key_name, event_hub_sas_key_value) 


print( "event hub connection string: ", event_hub_conn_str )

print( "event hub connection string: ", event_hub_topic )
