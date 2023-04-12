# Databricks notebook source
# MAGIC %run ./config_dbx_database

# COMMAND ----------

# MAGIC %run ./config_event_hub

# COMMAND ----------

# MAGIC %run ./util_transfer_ev_hub_to_lake

# COMMAND ----------

transfer_event_hub_to_deltalake( event_hub_conn_str, event_hub_topic, event_hub_topic_consumer_group, database_name, database_table_name, database_checkpoint_prefix )
