package zjucst.arch.domain.VO;

import lombok.Data;

@Data
public class ResultVO<T> {

    // status code
    private Integer code;

    // result message
    private String msg;

    // result data
    private T data;
}
