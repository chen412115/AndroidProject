package dec.genius.convert.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * @ClassName: TextViewUtils 
 * @Description: TODO
 * @author WangFusheng 
 * @date 2015年9月24日 上午10:08:11
 */
public class TextViewUtils {
	public static void setSpinnerTextView(Context context ,TextView tv,String content ,int styleId,int start,int end){
		   SpannableString styledText = new SpannableString(content);  
	       styledText.setSpan(new TextAppearanceSpan(context, styleId), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  
	       tv.setText(styledText, TextView.BufferType.SPANNABLE);  
	}
	public static void setSpinnerTextView(Context context ,TextView tv,String content ,int[] styleId,int[] start,int[] end){
			if(styleId==null || styleId.length == 0 ||start==null || start.length == 0 ||end==null || end.length == 0 || styleId.length != start.length ||styleId.length != end.length||end.length != start.length ){
				return;
			}
		   SpannableString styledText = new SpannableString(content); 
		   for(int i=0;i<styleId.length;i++){
			   styledText.setSpan(new TextAppearanceSpan(context, styleId[i]), start[i], end[i], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  
		   }
	       tv.setText(styledText, TextView.BufferType.SPANNABLE);  
	}
	public static String getEditTextView(EditText et){
		return et.getText().toString().trim();
	}
	
}
