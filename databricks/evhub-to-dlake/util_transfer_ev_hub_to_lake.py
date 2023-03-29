# Databricks notebook source
# MAGIC %md
# MAGIC ## Functions
# MAGIC #### transfer_event_hub_to_deltalake
# MAGIC #### write_stream_to_table - used by above

# COMMAND ----------

# utility function
def write_stream_to_table( database_name, table_name, checkpoint_prefix, df ):
    
    # database checkpoint for stream
    checkpt = f"{checkpoint_prefix}/{table_name}_checkpoint"
    
    # database full path
    database_path = f"{database_name}.{table_name}"
    
    df.writeStream\
        .format("delta")\
        .outputMode("append")\
        .trigger(availableNow=True)\
        .option("checkpointLocation", checkpt)\
        .toTable( database_path )
    

# COMMAND ----------

def transfer_event_hub_to_deltalake( event_hub_conn_str, event_hub_topic, event_hub_topic_consumer_group, database_name, database_table_name, database_checkpoint_prefix ):
    
    eh_conf = {}
    eh_conf['eventhubs.connectionString'] = sc._jvm.org.apache.spark.eventhubs.EventHubsUtils.encrypt( event_hub_conn_str )
    eh_conf['eventhubs.consumerGroup'] = event_hub_topic_consumer_group
    
    # read stream
    df = spark.readStream.format("eventhubs").options(**eh_conf).load()
    
    # payload
    df = df.withColumn("body", df["body"].cast("string"))
    
    #table name per topic name
    tbl_name = f"{event_hub_topic}_eh_raw".replace("-", "_").lower()
  
    write_stream_to_table( database_name, database_table_name, database_checkpoint_prefix, df )
