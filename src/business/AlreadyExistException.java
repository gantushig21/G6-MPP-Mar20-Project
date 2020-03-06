package business;

import java.io.Serializable;

public class AlreadyExistException extends Exception implements Serializable{
	public AlreadyExistException() {
		super();
	}
	public AlreadyExistException(String msg) {
		super(msg);
	}
	public AlreadyExistException(Throwable t) {
		super(t);
	}
	
	private static final long serialVersionUID = -1082101400359592831L;
}
