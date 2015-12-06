/* BookkeepingAlertDialog.java
* Created on 2011-8-30
*/
package org.android.bookkeeping.activity;

import java.lang.reflect.Method;
import java.util.List;

import org.android.bookkeeping.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class BookkeepingAlertDialog {
	
	public void show(Context context, int messageId){
		show(context, new int[]{1, 0}, messageId, new int[]{R.string.ok,0});
	}
	
	public void show(Context context, int[] buttons, int messageId, int[] btnTextIds){
		show(context, buttons, messageId, btnTextIds, null);
	}	
	
	public void show(Context context, int[] buttons, int messageId, int[] btnTextIds, final String[] methodsForPB){
		show(context, buttons, messageId, btnTextIds, methodsForPB, null);
	}
	
	/**
	 * This is used to show alert dialog which should have positive and negative button, default title. 
	 * On the click of positive button and negative button, there should be related methods without arguments to be called.
	 * @param context 
	 * @param messageId the id of the message which will be displayed in the alert dialog
	 * @param methodsForPB on the click of positive button, these methods will be getting called.
	 * @param methodsForNB on the click of negative button, these methods will be getting called.
	 */
	public void show(Context context, int messageId, int[] btnTextIds, final String[] methodsForPB, final String[] methodsForNB){
		show(context, new int[]{1, 1}, messageId, btnTextIds, methodsForPB, methodsForNB);
	}
	
	public void show(Context context, int[] buttons, int messageId, int[] btnTextIds, final String[] methodsForPB, final String[] methodsForNB){
		show(context, buttons, 0, messageId, btnTextIds, methodsForPB, null, null, methodsForNB, null, null);
	}	
	
	@SuppressWarnings("unchecked")
	public void show(final Context context, int[] buttons, int titleId,
			int messageId, int[] btnTextId, final String[] methodsForPB,
			final List<Class[]> paramsForPB, final Object[] valsForPB,
			final String[] methodsForNB, final List<Class[]> paramsForNB,
			final Object[] valsForNB) {
		
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(titleId == 0 ? R.string.tooltip : titleId);
		builder.setMessage(messageId);
		
		if(buttons[0] > 0){
			int buttonTextId = (btnTextId[0] != 0) ? btnTextId[0] : R.string.ok;
			builder.setPositiveButton(buttonTextId, new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(methodsForPB != null && methodsForPB.length > 0)	{
						for(int i = 0; i< methodsForPB.length; i++){
							try {
								if(paramsForPB != null && !paramsForPB.isEmpty()){
									Method method = context.getClass().getDeclaredMethod(methodsForPB[i], paramsForPB.get(i));
									method.invoke(context, valsForPB);
								}else{
									Method method = context.getClass().getDeclaredMethod(methodsForPB[i]);
									method.setAccessible(true);
									method.invoke(context);
								}
							} catch (Exception e) {
								e.printStackTrace();
							} 
						}
					}
				}
			});
		}
		
		if(buttons[1] > 0){
			int buttonTextId = (btnTextId[1] != 0) ? btnTextId[1] : R.string.cancel;
			builder.setNegativeButton(buttonTextId, new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(methodsForNB != null && methodsForNB.length > 0)	{
						for(int i = 0; i< methodsForNB.length; i++){
							try {
								if(paramsForNB != null && !paramsForNB.isEmpty()){
									Method method = context.getClass().getDeclaredMethod(methodsForNB[i], paramsForNB.get(i));
									method.invoke(context, valsForNB);
								}else{
									Method method = context.getClass().getDeclaredMethod(methodsForNB[i]);
									method.setAccessible(true);
									method.invoke(context);
								}
							} catch (Exception e) {
								e.printStackTrace();
							} 
						}
					}
				}
			});
		}
		
		builder.show();
	}
}
