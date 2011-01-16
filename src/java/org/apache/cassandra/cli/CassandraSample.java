package org.apache.cassandra.cli;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnPath;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class CassandraSample {
	public static final String UTF8 = "UTF-8";
	

	public static void main(String[] args) throws InvalidRequestException,
			NotFoundException, UnavailableException, TimedOutException,
			TException, UnsupportedEncodingException {
		
		//make trash data
		String trash =  "hallo!hallo!hallo!hallo!hallo!hallo!hallo!hallo!hallo!hallo!";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 100; i++) {
			 
			 sb.append(trash);
		}
		
		String data = sb.toString();
		byte[] value = 
			data.getBytes(UTF8);
		//data-size is 6kb.
		String arg0 = args[0];
		int counter= Integer.valueOf(arg0);
		System.out.println("each data-size we inserted are " +  value.length  + "byte");
		
		TTransport transport = new TSocket("127.0.0.1", 9161);
		TProtocol protocol = new TBinaryProtocol(transport);
		Cassandra.Client client = new Cassandra.Client(protocol);
		
		transport.open();
		try {
			for (int i = 0; i < counter; i++) {
				String keyspace = "Keyspace1";
				String columnFamily = "Standard2";
				String columnAge = "age"+ i;
				String key = "jsmith"+ i/10;
				long timestamp = System.currentTimeMillis() * 1000;

				ColumnPath colPathAge = new ColumnPath(columnFamily)
						.setColumn(columnAge.getBytes(UTF8));
				client.insert(keyspace, key, colPathAge, value, timestamp,
						ConsistencyLevel.ONE);
//				Column col = client.get(keyspace, key, colPathAge,
//						ConsistencyLevel.ONE).getColumn();
//
//				System.out.println(col);
//				System.out.println(new String(col.getName(), UTF8));
//				System.out.println(new String(col.getValue(), UTF8));
//				System.out.println(new Date(col.getTimestamp() / 1000));
			}
		} finally {
			transport.close();
		}
		System.out.println("the data-size we inserted are " +  value.length * counter  + "byte");
	}
}