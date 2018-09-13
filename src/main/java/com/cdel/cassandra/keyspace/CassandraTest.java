package com.cdel.cassandra.keyspace;

public class CassandraTest {

	public static void create(){
		// 创建客户端
		CassandraClient cassandraClient = new CassandraClient("test");

		String query = "CREATE KEYSPACE tp WITH replication " + "= {'class':'SimpleStrategy', 'replication_factor':1}; ";
		//您可以使用Session类的execute（）方法执行CQL查询。将查询以字符串格式或Statement类对象传递给execute（）方法。无论您以字符串格式传递给此方法将在cqlsh上执行。
		//在这个例子中，我们创建一个名为tp的KeySpace。我们使用第一个副本放置策略，即简单策略，我们选择复制因子为1个副本。
		//您必须将查询存储在字符串变量中，并将其传递给execute（）方法，如下所示。
		cassandraClient.executeSQL(query);
		//您可以使用execute（）方法使用创建的KeySpace，如下所示。
		cassandraClient.executeSQL("USE tp");

		// 关闭
		cassandraClient.close();
	}

	public static void update(){
		// 创建客户端
		CassandraClient cassandraClient = new CassandraClient("test");

		String query = "ALTER KEYSPACE tp WITH replication " + "= {'class':'NetworkTopologyStrategy', 'datacenter1':3} AND DURABLE_WRITES = false;";
		cassandraClient.executeSQL(query);

		// 关闭
		cassandraClient.close();
	}

	public static void delete(){
		// 创建客户端
		CassandraClient cassandraClient = new CassandraClient("test");

		String query = "DROP KEYSPACE tp; ";
		cassandraClient.executeSQL(query);

		// 关闭
		cassandraClient.close();

	}

	public static void main(String[] args) {
		delete();
    }

}
