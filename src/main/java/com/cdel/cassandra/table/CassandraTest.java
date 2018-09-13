package com.cdel.cassandra.table;

import com.cdel.cassandra.keyspace.CassandraClient;

public class CassandraTest {

	public static void create(){
		CassandraClient cassandraClient = new CassandraClient("test");

		//Query
		String query = "CREATE TABLE emp(emp_id int PRIMARY KEY, "
	         + "emp_name text, "
	         + "emp_city text, "
	         + "emp_sal varint, "
	         + "emp_phone varint );";
		cassandraClient.executeSQL(query);

		// 关闭
		cassandraClient.close();
	}

	public static void update(){
		// 创建客户端
		CassandraClient cassandraClient = new CassandraClient("test");

		String query = "ALTER TABLE emp ADD emp_email text";
		//String query = "ALTER TABLE emp DROP emp_email;";
		cassandraClient.executeSQL(query);

		// 关闭
		cassandraClient.close();
	}

	public static void delete(){
		// 创建客户端
		CassandraClient cassandraClient = new CassandraClient("test");

		String query = "DROP TABLE emp1;";
		//String query = "Truncate student;";
		cassandraClient.executeSQL(query);

		// 关闭
		cassandraClient.close();

	}

	public static void createIndex(){
		// 创建客户端
		CassandraClient cassandraClient = new CassandraClient("test");

		String query = "CREATE INDEX name ON emp (emp_name);";
		cassandraClient.executeSQL(query);

		// 关闭
		cassandraClient.close();

	}

	public static void dropIndex(){
		// 创建客户端
		CassandraClient cassandraClient = new CassandraClient("test");

		String query = "DROP INDEX name;";
		cassandraClient.executeSQL(query);

		// 关闭
		cassandraClient.close();

	}

	public static void batch(){
		// 创建客户端
		CassandraClient cassandraClient = new CassandraClient("test");

		String query =" BEGIN BATCH INSERT INTO emp (emp_id, emp_city, emp_name, emp_phone, emp_sal) values( 4,'Pune','rajeev',9848022331, 30000);"
	         + "UPDATE emp SET emp_sal = 50000 WHERE emp_id =3;"
	         + "DELETE emp_city FROM emp WHERE emp_id = 2;"
	         + "APPLY BATCH;";
		cassandraClient.executeSQL(query);

		// 关闭
		cassandraClient.close();

	}

	public static void main(String[] args) {
		batch();
    }

}
