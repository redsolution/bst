package ru.redsolution.bst.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

/**
 * Логгирует полученные данные в файл.
 * 
 * @author alexander.ivanov
 * 
 */
public class DebugReader extends Reader {

	final Reader wrappedReader;
	private FileWriter writer;

	public DebugReader(Reader wrappedReader, File file) {
		this.wrappedReader = wrappedReader;
		try {
			writer = new FileWriter(file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	void onRead(char[] buf, int offset, int count) {
		if (count > 0) {
			try {
				writer.write(buf, offset, count);
				writer.flush();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public int read(char[] buf, int offset, int count) throws IOException {
		final int result = wrappedReader.read(buf, offset, count);
		onRead(buf, offset, count);
		return result;
	}

	@Override
	public void close() throws IOException {
		wrappedReader.close();
		writer.close();
	}

	@Override
	public int read() throws IOException {
		return wrappedReader.read();
	}

	@Override
	public int read(char buf[]) throws IOException {
		return wrappedReader.read(buf);
	}

	@Override
	public long skip(long n) throws IOException {
		return wrappedReader.skip(n);
	}

	@Override
	public boolean ready() throws IOException {
		return wrappedReader.ready();
	}

	@Override
	public boolean markSupported() {
		return wrappedReader.markSupported();
	}

	@Override
	public void mark(int readAheadLimit) throws IOException {
		wrappedReader.mark(readAheadLimit);
	}

	@Override
	public void reset() throws IOException {
		wrappedReader.reset();
	}
}
