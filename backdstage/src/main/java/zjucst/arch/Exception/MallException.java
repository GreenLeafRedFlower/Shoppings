package zjucst.arch.Exception;

import lombok.Getter;
import lombok.Setter;

public class MallException extends RuntimeException {

    @Getter
    @Setter
    private Integer code;

    public MallException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
