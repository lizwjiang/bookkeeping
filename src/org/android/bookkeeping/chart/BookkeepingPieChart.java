/* ItemPieChart.java
* Created on 2011-8-10
*/
package org.android.bookkeeping.chart;

import org.achartengine.ChartFactory;
import org.achartengine.renderer.DefaultRenderer;
import org.android.bookkeeping.common.chart.AbstractChart;
import org.android.bookkeeping.util.Constants;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
/**
 * This is a common class for pie chart. by calling execute method, a pie view 
 * will be returned.
 * 
 * @author Johnson_Jiang01
 * @version 1.0, 2011-9-20
 */
public class BookkeepingPieChart extends AbstractChart {
	private String NAME;
	private String DESC;
	
	@Override
	public View execute(Context context, Bundle bundle) {
		String[] categories = bundle.getStringArray(Constants.STATISTICS_CATEGORY_KEY);
	    double[] values = bundle.getDoubleArray(Constants.STATISTICS_VALUE_KEY);
	    int[] colors = getRequiredColors(categories.length);
	    DefaultRenderer renderer = buildCategoryRenderer(colors);
	    renderer.setZoomButtonsVisible(true);
	    renderer.setZoomEnabled(true);
	    renderer.setChartTitleTextSize(20);
	    
		return ChartFactory.getPieChartView(context, buildCategoryDataset(
				bundle.getString(Constants.STATISTICS_CATEGORY_TITLE_KEY),
				categories, values), renderer);
	}

	int[] getRequiredColors(int count){		
		int[] colors = new int[] { Color.BLUE, Color.GREEN, Color.MAGENTA,
				Color.YELLOW, Color.CYAN, Color.GRAY, Color.LTGRAY, Color.RED };
		int[] chartColors = new int[count];
				
		for (int i = 0; i < count; i++) {
			chartColors[i] = colors[i % (colors.length-1)];			
		}
		return chartColors;
	}
	
	@Override
	public String getDesc() {
		return DESC;
	}

	@Override
	public String getName() {
		return NAME;
	}

}
