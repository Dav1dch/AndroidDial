package com.tuffy.dial;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * @author david
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    String[] colorArray = {"#EF9A9A", "#A5D6A7", "#CE93D8", "#9FA8DA", "#90CAF9", "#FFAB91"};
    TextView numText;
    private List<MyContacts> myContactsList;
    static class ViewHolder extends RecyclerView.ViewHolder{
       TextView contactName;
       TextView contactPhone;
       TextView Firstletter;

       public ViewHolder(@NonNull View itemView) {
           super(itemView);
           contactName = itemView.findViewById(R.id.contact_name);
           contactPhone = itemView.findViewById(R.id.contact_phoneNum);
           Firstletter = itemView.findViewById(R.id.First_letter);
       }
    }
   public  ContactAdapter(List<MyContacts> contactsList){
       myContactsList = contactsList;
   }

   public ContactAdapter(List<MyContacts> contactsList, TextView view){
        myContactsList = contactsList;
        numText = view;

   }


    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ContactAdapter.ViewHolder holder, int position) {
       MyContacts contact = myContactsList.get(position);
       holder.contactName.setText(contact.getName());
       holder.contactPhone.setText(contact.getNum());
       holder.Firstletter.setText(FirstCharUtil.first(contact.getName()));
       holder.Firstletter.setTextColor(Color.parseColor(colorArray[((int)FirstCharUtil.first(contact.getName()).charAt(0))%6]));
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               numText.setText(holder.contactPhone.getText());
           }
       });

    }

    @Override
    public int getItemCount() {
        return myContactsList.size();
    }
}
