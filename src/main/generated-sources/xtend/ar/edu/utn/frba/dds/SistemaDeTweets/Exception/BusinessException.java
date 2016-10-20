package ar.edu.utn.frba.dds.SistemaDeTweets.Exception;

@SuppressWarnings("all")
public class BusinessException extends RuntimeException {
  public BusinessException(final String msg) {
    super(msg);
  }
}
