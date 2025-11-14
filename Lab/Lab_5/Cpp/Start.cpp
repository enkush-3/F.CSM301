#include<bits/stdc++.h>


using namespace std;

struct Number {
    int value;
    int deep;
};

class Lab_5_3{
    public:

    int maxSearch(vector<Number> &vec, int start, int end)
    {
        if(start >= end){
            return start;
        }

        int mid = start + (end - start) / 2;

        int left = maxSearch(vec, start, mid);
        int right = maxSearch(vec, mid + 1, end);

        return vec[left].value < vec[right].value ? right: left;
    }

    vector<Number> solutionFunc(vector<Number> &vec, int start, int end, int deep)
    {
        if(start > end){
            return vec;
        }

        int maxIndex = maxSearch(vec, start, end);
        vec[maxIndex].deep = deep;

        solutionFunc(vec, start, maxIndex - 1, deep + 1);
        solutionFunc(vec, maxIndex + 1, end, deep + 1);

        return vec;
    }

};

int main()
{
    int n ;

    cin >> n;

    while(n--)
    {
        int n1;
        cin >> n1;
        vector <Number> myvec;
        

        for(int i=0; i< n1; i++)
        {
            int c;
            cin >> c;
            myvec.push_back({c, 0});
        }
        
        Lab_5_3 sol;
        vector<Number> resultVec = sol.solutionFunc(myvec, 0, myvec.size() - 1, 0);

        for (auto &n : resultVec)
            cout << n.deep << " ";
        cout<< endl;
    }
}

/*

3
5
3 5 2 1 4
1
1
4
4 3 1 2


*/