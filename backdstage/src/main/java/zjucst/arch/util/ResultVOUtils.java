package zjucst.arch.util;

import zjucst.arch.domain.VO.ResultVO;

public class ResultVOUtils {

    public static ResultVO success(Object obj) {
        ResultVO resultVO = new ResultVO<>();
        resultVO.setData(obj);
        resultVO.setCode(0);
        resultVO.setMsg("success");
        return resultVO;
    }

    public static ResultVO<Object> success() {
        return success(null);
    }

    public static ResultVO error(Integer code, String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }
}
