package persistence;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringEscapeUtils;
import org.repodriller.RepoDrillerException;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.util.PathUtils;

public class SQLFile implements PersistenceMechanism {
	private String[] columns;
	private boolean isOpen = false;
	private PrintStream ps;
	private String tableName;
	private List<String[]> valuesBuffer = new ArrayList<>();
	private int insertsPerQuery = 1000;

	public SQLFile(String fileName, String tableName, String[] columns, boolean append) {
		open(fileName, append);
		this.columns = columns;
		this.tableName = tableName;
	}
	
	@Override
	public void write(Object... fields) {
		if (!isOpen)
			throw new RepoDrillerException("Error, writing to a closed SQL file.");

		if (columns.length != fields.length)
			throw new RepoDrillerException("SQL file has " + columns.length + " columns but you provided " + fields.length + " fields");
		
		String[] escapedFields = escapeFields(fields);
		valuesBuffer.add(escapedFields);
		
		if (valuesBuffer.size() >= this.insertsPerQuery) {
			flushValuesBuffer();
		}
	}
	
	private void flushValuesBuffer() {
		if (valuesBuffer.size() == 0)
			return;
		StringBuilder queryBuilder = new StringBuilder()
				.append("INSERT INTO " + tableName + " (" + String.join(",", columns) + ") VALUES");
		for (String[] fields : valuesBuffer) {
			queryBuilder.append("\n\t(" + String.join(", ", fields) + "),");
		}
		queryBuilder.replace(queryBuilder.length()-1, queryBuilder.length(), ";");
		ps.println(queryBuilder.toString());
		ps.flush();
		valuesBuffer.clear();
	}

	private String[] escapeFields(Object... fields) {
		return Stream.of(fields).map(field -> {
			if (field == null)
				return "null";
			else {
				String raw = field.toString();
				if (raw == null || raw.length() == 0)
					return "null";
				return "\"" + StringEscapeUtils.escapeCsv(raw) + "\"";
			}
		})
		.toArray(String[]::new);
	}

	@Override
	public void close() {
		if (isOpen) {
			if (this.valuesBuffer.size() > 0) {
				flushValuesBuffer();
			}
			ps.close();
			isOpen = false;
		}
	}
	
	private void open(String fileName, boolean append) {
		try {
			fileName = PathUtils.fullPath(fileName);
			ps = new PrintStream(new FileOutputStream(fileName, append));
			isOpen = true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
