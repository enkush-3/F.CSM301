#include <bits/stdc++.h>

using namespace std;

class Lab_5_2{
public:
    static const int MOD = 1337;

    int powMe(int a, int b){
        int oddResult = 1;

        a%=MOD;

        while(b > 0){
            if(b % 2 == 1)
                oddResult = (oddResult * a) % MOD;

            a = (a*a) % MOD;
            b/=2;
        }
        return oddResult;
    }

    int superPow(int a, vector<int>& b) {
        if(b.empty())
            return 1;
        
        int last = b.back();
        b.pop_back();

        int lastPow = powMe(a, last);
        int leftPow = powMe(superPow(a, b), 10);

        return (lastPow * leftPow) % MOD;
    }
};

int main()
{
    Lab_5_2 sol;

    vector <int> vec = {1, 0};

    cout << sol.superPow(2, vec);
}