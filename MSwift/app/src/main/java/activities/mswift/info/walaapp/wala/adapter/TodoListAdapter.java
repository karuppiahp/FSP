package activities.mswift.info.walaapp.wala.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.model.TodoListModel;

/**
 * Created by karuppiah on 11/24/2015.
 */
public class TodoListAdapter extends BaseAdapter {

    private Context context;
    private List<TodoListModel> todoNameArray = new ArrayList<>();

    public TodoListAdapter(Context context, List<TodoListModel> todoNameArray) {
        this.context = context;
        this.todoNameArray = todoNameArray;
    }

    @Override
    public int getCount() {
        return todoNameArray.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View v, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        v = inflater.inflate(R.layout.todo_list_items, null);
        TextView txtForName = (TextView) v.findViewById(R.id.txtForTodoListItems);
        TodoListModel todoListNames = todoNameArray.get(i);
        txtForName.setText(todoListNames.getName());
        return v;
    }
}
