/* BookkeepingStatistics.java
* Created on 2011-9-7
*/
package org.android.bookkeeping.activity.statistics;

import org.android.bookkeeping.R;
import org.android.bookkeeping.chart.BookkeepingPieChart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class StatisticsChart extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		RelativeLayout layout = new RelativeLayout(this); 
		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		lp1.addRule(RelativeLayout.ALIGN_PARENT_TOP); 
		lp1.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE); 

		Bundle bundle = getIntent().getExtras();
		View view = new BookkeepingPieChart().execute(this, bundle);
		layout.addView(view, lp1); 
		
		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.ABOVE, view.getId()); 
        lp2.addRule(RelativeLayout.ALIGN_LEFT, view.getId()); 
        
        Button b = new Button(this);
		b.setText(getResources().getText(R.string.ok));
		b.setOnClickListener(okBtnOnClickListener);
        layout.addView(b, lp2); 
		
        setContentView(layout);
	}

	
	private OnClickListener okBtnOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {			
			Intent intent = new Intent();
			intent.setClass(StatisticsChart.this, StatisticsQuery.class);
			startActivity(intent);
		}
	};
}
