package Scientific;
/**
 * @author Bu Xinran
 * @Description 科学计算器计算式可能出现的错误
 * @date 2023/11/23 22:44
 */
public enum ErrorScientific {
    //yes:没有错误。symbolContinue:符号连续（eg：×+-)。bracket：括号匹配错误。BrackNull：括号内没有东西。error:错误太多，常规显示为错误，
    yes, symbolContinue, bracket,pow,dotRepeat,nothing,divideZero,Illegal,BrackNull,error,
}
