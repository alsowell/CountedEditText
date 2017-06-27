# CountedEditText
edittext with counted feature ;two count mode
项目中简单的计数edittext  支持两种计数模式
# 参数说明
```java
  <declare-styleable name="CountedEditText">
        <attr name="maxNum" format="integer"/><!--最大可输入数量-->
        <attr name="mode" format="enum"><!--计数模式-->
            <enum name="normal" value="0"/>
            <enum name="whitSprit" value="1"/>
        </attr>
        <attr name="countColor" format="color"></attr><!--正常计数显示颜色-->
        <attr name="countSize" format="dimension"/><!--计数size-->
        <attr name="overCountColor" format="color"/><!--超过最大计数显示颜色-->
        <attr name="paddingToRight" format="dimension"/><!--右间距-->
        <attr name="paddingToBottom" format="dimension"/><!--底部间距-->
    </declare-styleable>
  
