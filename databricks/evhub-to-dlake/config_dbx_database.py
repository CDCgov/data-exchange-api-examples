# Databricks notebook source
# MAGIC %md
# MAGIC ## Database configuration
# MAGIC 
# MAGIC #### database_name
# MAGIC #### database_table_name
# MAGIC #### database_checkpoint_prefix

# COMMAND ----------


database_name = "todo_database"
database_table_name = "todo_topic"
database_checkpoint_prefix = "todo_chck_pref"


# COMMAND ----------

print( "databricks database name: ", database_name )

print( "databricks table name: ", database_table_name )

print( "databricks database checkpoint prefix name: ", database_checkpoint_prefix )

# COMMAND ----------


