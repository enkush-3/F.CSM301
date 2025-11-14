#include <bits/stdc++.h>

using namespace std;

class Lab_5_1 {
    public:
        string longestNiceSubstring(string str)
        {
            if(str.length() < 2)
                return "";

            bool upper[26] = {false}, lower[26] = {false};

            for(char i: str)
                if(i < 97)
                    upper[i - 'A'] = true;
                else
                    lower[i - 'a'] = true;

            for(int i=0; i<str.length(); i++){
                int boolindex = str[i] < 97 ? str[i] - 'A': str[i] - 'a';

                if(upper[boolindex] != lower[boolindex]){
                    string left = longestNiceSubstring(str.substr(0, i));
                    string right = longestNiceSubstring(str.substr(i + 1));

                    return left.length() < right.length() ? right: left;
                }
            }
            return str;
        }
};

int main(){
    string str;
    cin >> str;

    Lab_5_1 sol ;
    cout<< sol.longestNiceSubstring(str);
}
                