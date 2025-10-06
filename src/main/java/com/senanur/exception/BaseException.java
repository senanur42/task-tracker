// jwt yerine ke clock geldiği için bu class artık kullanılmıyor

package com.senanur.exception;

public class BaseException extends RuntimeException{

    public BaseException(ErrorMessage errorMessage){
        super(errorMessage.prepareErrorMessage());

    }

}
