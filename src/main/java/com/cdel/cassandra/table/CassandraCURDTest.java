package com.cdel.cassandra.table;

import com.cdel.cassandra.keyspace.CassandraClient;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

public class CassandraCURDTest {

	public static void insert(){
		CassandraClient cassandraClient = new CassandraClient("test");

		String query1 = "INSERT INTO emp (emp_id, emp_name, emp_city, emp_phone,  emp_sal)"
				+ " VALUES(1,'ram', 'Hyderabad', 9848022338, 50000);" ;

		String query2 = "INSERT INTO emp (emp_id, emp_name, emp_city, emp_phone, emp_sal)"
				+ " VALUES(2,'robin', 'Hyderabad', 9848022339, 40000);" ;

      	String query3 = "INSERT INTO emp (emp_id, emp_name, emp_city, emp_phone, emp_sal)"
      			+ " VALUES(3,'rahman', 'Chennai', 9848022330, 45000);" ;

		cassandraClient.executeSQL(query1);
		cassandraClient.executeSQL(query2);
		cassandraClient.executeSQL(query3);

		// 关闭
		cassandraClient.close();
	}

	public static void update(){
		CassandraClient cassandraClient = new CassandraClient("test");

		String query = " UPDATE emp SET emp_city='Delhi',emp_sal=50000 WHERE emp_id = 2;";
		cassandraClient.executeSQL(query);

		// 关闭
		cassandraClient.close();
	}

	public static void select(){
		CassandraClient cassandraClient = new CassandraClient("test");

		String query = "SELECT * FROM emp";
		ResultSet result = cassandraClient.executeSQL(query);
		// 输出
        Row row = result.one();
        System.out.println("row=" + row.toString());
        System.out.println("emp_city=" + row.getString("emp_city"));
        System.out.println("emp_email=" + row.getString("emp_email"));

		// 关闭
		cassandraClient.close();
	}

	public static void main(String[] args) {
		select();
    }

}
