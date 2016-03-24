
package pl.smsapi.exception;


public class ClientException extends SmsapiException{
	
	private int code;
	
	public ClientException(String message){
		super(message);
	}
	
	public ClientException(String message, int code){
		this(message);
		this.code = code;
	}
	
	public int getCode(){
		return code;
	}
	
}