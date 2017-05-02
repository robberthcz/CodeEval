module Main where

import Data.Array

main = do
    l1 <- getLine
    l2 <- getLine
    let [n,k] = map (\x -> read x :: Int) $ words l1 
    let twrs = map (\x -> read x :: Int) $ words l2
    putStrLn $ show $ greedy twrs n k



greedy :: [Int] -> Int -> Int -> Int
greedy twrs n k = greedy' (listArray (1,n) twrs) (1,k) 0
    where greedy' :: Array Int Int -> (Int, Int) -> Int -> Int
          greedy' a (min,max) count | (max < min) = -1
                                    | (a!max == 0) = greedy' a (min,max-1) count
                                    | (a!max == 1) = if(max+k-1 >= n) then (count+1) else greedy' a (max+1,upBound) (count+1)
                                    | otherwise = count
                 where upBound = if((max+2*k-1) <= n) then (max+2*k-1) else n