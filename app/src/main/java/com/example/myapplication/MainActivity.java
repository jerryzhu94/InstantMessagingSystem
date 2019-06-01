package com.example.myapplication;

import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

//controller
public class MainActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    EditText usernameText, passwordText, chatIdText;
    Button createUserButton, createChatButton, joinChatButton, loginButton;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set view
        setContentView(R.layout.activity_main);

        //initialize dataMapper(Persistence layer)
        dbHelper = new DatabaseHelper(this);
        //user is null until login is successful
        user = null;

        //define interface components
        usernameText = (EditText)findViewById(R.id.editText_username);
        passwordText = (EditText)findViewById(R.id.editText_password);
        chatIdText = (EditText)findViewById(R.id.editText_chat_id);
        createUserButton = (Button)findViewById(R.id.button_createAccount);
        createChatButton = (Button)findViewById(R.id.button_createChat);
        joinChatButton = (Button)findViewById(R.id.button_joinChat);
        loginButton = (Button)findViewById(R.id.button_join);

        //set behaviors of interface components
        CreateAccount();
        CreateChat();
        JoinChat();
        Login();
    }

    //defines behavior of 'Create Account' button
    public void CreateAccount(){
        createUserButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        String username = usernameText.getText().toString();
                        String password = passwordText.getText().toString();

                        //attempt to insert new user to User database table
                        boolean isInserted = dbHelper.insertUser(username, password);

                        if(isInserted == true){
                            Toast.makeText(MainActivity.this, "User Inserted", Toast.LENGTH_LONG).show();
                        } else{
                            Toast.makeText(MainActivity.this, "User Not Inserted", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    //defines behavior of 'Create Chat' button
    public void CreateChat(){
        createChatButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        //remove this if statement in the future
                        if(user == null) {
                            Toast.makeText(MainActivity.this, "Need to login first", Toast.LENGTH_LONG).show();
                            return;
                        }
                        String username = user.getUserName();
                        String chatId = chatIdText.getText().toString();

                        //attempt to insert new chat to Chat database table
                        boolean isInserted = dbHelper.insertChat(chatId);

                        if(isInserted == true){
                            //attempt to insert new entry to Chat_User database table
                            dbHelper.insertChatUser(username, chatId);
                            //add new chat to user's chats array
                            ArrayList<Chat> chats  = user.getChats();
                            chats.add(new Chat(chatId));
                            Toast.makeText(MainActivity.this, "Chat Inserted", Toast.LENGTH_LONG).show();
                        } else{
                            Toast.makeText(MainActivity.this, "Chat Not Inserted", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    //defines behavior of 'Create Chat' button
    public void JoinChat(){
        joinChatButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        //remove this if statement in the future
                        if(user == null) {
                            Toast.makeText(MainActivity.this, "Need to login first", Toast.LENGTH_LONG).show();
                            return;
                        }
                        String username = user.getUserName();
                        String chatId = chatIdText.getText().toString();

                        //check whether chat exists
                        boolean isExistingChat = dbHelper.isExistingChat(chatId);

                        if(isExistingChat == true){
                            //attempt to insert new entry to Chat_User database table
                            dbHelper.insertChatUser(username, chatId);
                            //add new chat to user's chats array
                            ArrayList<Chat> chats  = user.getChats();
                            chats.add(new Chat(chatId));
                            Toast.makeText(MainActivity.this, "Joined Chat Successful", Toast.LENGTH_LONG).show();
                        } else{
                            Toast.makeText(MainActivity.this, "Joined Chat Unsuccessful", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }


    //defines behavior of 'Login' button
    public void Login(){
        loginButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        String username = usernameText.getText().toString();
                        String password = passwordText.getText().toString();

                        boolean isExistingUser = dbHelper.isExistingUser(username, password);

                        if(isExistingUser == true){
                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                            //create user and user's chat instances
                            user = new User(username, password);
                            ArrayList<String> chatIds = dbHelper.getUserChatIds(username);
                            ArrayList<Chat> chats = new ArrayList<Chat>();
                            for(String chatId : chatIds)
                                chats.add(new Chat(chatId));
                            user.setChats(chats);
                        } else{
                            Toast.makeText(MainActivity.this, "Login Unsuccessful", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }
}
