import java.io.IOException;

public class EmptyFileException extends IOException {
    public EmptyFileException(String error) {
        super(error);
    }
}
