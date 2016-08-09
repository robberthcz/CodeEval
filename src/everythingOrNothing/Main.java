/*      EVERYTHING OR NOTHING
        CHALLENGE DESCRIPTION:

        Today, you have a top-secret assignment. You will have access to a super confidential information about documents with the highest security rate. There are only three such documents in the world which include extremely important information. If any of these files fall into the wrong hands, there is a risk that the global balance of power can be destroyed.
        Only 6 people have access to these files, each with the different access rights—from a total ban to a full access. Several lines of code from these files leaked into the Internet recently. Your task will be to check the code that we provide. But, be careful: if you agree to take this task, you automatically sign an agreement on non-disclosure of confidential information; otherwise, we will have to … you.

        The following is a transcript of all rights to the files:
        0 - total ban;
        1 - grant;
        2 - write;
        3 - write, grant;
        4 - read;
        5 - read, grant;
        6 - read, write;
        7 - full access (read, write, grant);

        The following table shows what access to a particular file each of these 6 users had.


        
        1
        2
        3
        4
        5
        6
        7
        file_1   file_2   file_3
        user_1     7        3        0
        user_2     6        2        4
        user_3     5        1        5
        user_4     3        7        1
        user_5     6        0        2
        user_6     4        2        6
        INPUT SAMPLE:

        The first argument is a path to a file. Each line includes a test case with several lines of code separated by a space. In each line, first comes the user name, then the file name, and finally what the user done with this file.
        If it is granting an access to someone, then the code contains the action that is granted followed by a name of a user who has been granted access. For each test case, a table above will be a starting point. That is, if the action is user_3=>file_3=>grant=>read=>user_1, the number in the table for user_1 will change from 0 to 4. However, for the next test cases, it will again be 0.

        For example:


        
        1
        2
        3
        user_1=>file_1=>read user_2=>file_2=>write
        user_1=>file_1=>grant=>read=>user_4 user_4=>file_1=>read
        user_4=>file_1=>read
        OUTPUT SAMPLE:

        You must go through all the lines of code, and if at least one is not correct, print False; otherwise, bring True.


        
        1
        2
        3
        True
        True
        False
        CONSTRAINTS:

        The table with users is the same for each test case, but it can change within a test case depending on the code.
        The number of lines of code in a test case can be from 1 to 8.
        User with grant permissions can provide rights on read and write to himself.
        The number of test cases is 40.
*/
package everythingOrNothing;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Created by Rob on 8/9/2016.
 */
public class Main {
    private byte[][] acRightsTable;
    public Main(){
        acRightsTable = new byte[][]{ {7,3,0}, {6,2,4}, {5,1,5}, {3,7,1}, {6,0,2}, {4,2,6}};
    }

    public boolean processEvent(String event){
        int user1 = Integer.parseInt(event.substring(5, 6)) - 1;
        int file1 = Integer.parseInt(event.substring(13, 14)) - 1;
        char action = event.charAt(16);
        char grantAction = 0;
        int grantUser = 0;
        //System.out.println("User: " + user1 + "; File: " + file1 + "; Action: " + action );
        //System.out.println(isAllowed(user1, file1, action));
        if(action == 'g'){
            grantAction = event.charAt(23);
            grantUser = Integer.parseInt(event.substring(event.length() - 1, event.length())) - 1;
            if(!isAllowed(user1, file1, action))
                return false;
            else{
                updateTable(file1, grantAction, grantUser);
                return true;
            }

        }
        else
            return isAllowed(user1, file1, action);
    }

    private void updateTable(int file1, char grantAction, int grantUser){
        if(grantAction == 'r' && acRightsTable[grantUser][file1] < 4){
            acRightsTable[grantUser][file1] += 4;
        }
        else if(grantAction == 'w' && (acRightsTable[grantUser][file1] == 0 || acRightsTable[grantUser][file1] == 1 || acRightsTable[grantUser][file1] == 4 || acRightsTable[grantUser][file1] == 5)){
            acRightsTable[grantUser][file1] += 2;
        }
        else if(grantAction == 'g' && acRightsTable[grantUser][file1] % 2 == 0)
            acRightsTable[grantUser][file1] += 1;
    }

    private boolean isAllowed(int user1, int file1, char action){
        int right = acRightsTable[user1][file1];
        if(action == 'r')
            return 4 <= right;
        else if(action == 'w'){
            return right == 2 || right == 3 || right == 6 || right == 7;
        }
        else
            return right % 2 == 1;
    }

    public static void main(String[] args) throws FileNotFoundException {

        Scanner textScan = new Scanner(new FileReader("test-cases/everythingOrNothing.txt"));

        while(textScan.hasNextLine()){
            String line = textScan.nextLine();
            String[] events = line.split(" ");
            Main test = new Main();
            boolean result = true;
            for(String event : events) {
                if(!test.processEvent(event)){
                    result = false;
                    break;
                }
            }
            if(result)
                System.out.println("True");
            else
                System.out.println("False");
        }
    }
}
