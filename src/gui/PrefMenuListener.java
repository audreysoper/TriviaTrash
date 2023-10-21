package gui;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.MenuItem;

public class PrefMenuListener extends SelectionAdapter {
    private MenuItem menuItem;
    private boolean relatedValue;
    private String name;
    public PrefMenuListener(MenuItem item, boolean val, String text){
        this.menuItem=item;
        this.relatedValue=val;
        this.name=text;
    }
    @Override
    public void widgetSelected(SelectionEvent e) {
        relatedValue = !relatedValue;
        //userPrefs.putBoolean(name, relatedValue);
        menuItem.setSelection(relatedValue);
        AppBoard.userPrefs.putBoolean(name, relatedValue);
        AppBoard.redraw();

    }
}
