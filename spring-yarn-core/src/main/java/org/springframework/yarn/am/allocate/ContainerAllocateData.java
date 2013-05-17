package org.springframework.yarn.am.allocate;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class ContainerAllocateData {

	private Integer anyData = 0;
	private Map<String, Integer> hostData = new ConcurrentHashMap<String, Integer>();
	private Map<String, Integer> rackData = new ConcurrentHashMap<String, Integer>();

	public ContainerAllocateData() {
	}

	public void addRacks(String name, int count) {
		synchronized (rackData) {
			Integer value = rackData.get(name);
			if (value == null) {
				rackData.put(name, count);
			} else {
				rackData.put(name, value + count);
			}
		}
	}

	public Map<String, Integer> getRacks() {
		return rackData;
	}

	public void addHosts(String name, int count) {
		synchronized (hostData) {
			Integer value = hostData.get(name);
			if (value == null) {
				hostData.put(name, count);
			} else {
				hostData.put(name, value + count);
			}
		}
	}

	public Map<String, Integer> getHosts() {
		return hostData;
	}

	public void addAny(int count) {
		synchronized (anyData) {
			anyData = anyData + count;
		}
	}

	public int getAny() {
		return anyData;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();

		buf.append("[anyData=" + anyData + ", ");

		buf.append("hostData=[size=" + hostData.size() + ", ");
		buf.append("{");
		for (Entry<String, Integer> entry : hostData.entrySet()) {
			buf.append(entry.getKey() + "=" + entry.getValue() + " ");
		}
		buf.append("}]");

		buf.append("rackData=[size=" + rackData.size() + ", ");
		buf.append("{");
		for (Entry<String, Integer> entry : rackData.entrySet()) {
			buf.append(entry.getKey() + "=" + entry.getValue() + " ");
		}
		buf.append("}]]");

		return buf.toString();
	}

}
