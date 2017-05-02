module Main where

import Data.Array
import Data.List


main = do
     ls <- getContents
     let n:k:candies = map (\x -> read x :: Int) $ lines ls
     putStrLn $ show $ getMin (sort candies) n k


movingSum :: Array Int Int -> Int -> Int -> [Int]
movingSum a n k = reverse $ foldl f [init] [(k+1)..n]
     where init = sum [a!i | i <- [1..k]]
           f acc z = ((head acc) + a!z - a!(z-k)):acc

getMin :: [Int] -> Int -> Int -> Int
getMin cs n k = minimum $ foldl f [init] (zip [(k+1)..n] sums)
     where len = length cs
           acs = listArray (1, len) cs
           sums = drop 1 $ movingSum acs n (k-1)
           init = sum [acs!j - acs!i | i <- [1..k], j <- [(i+1)..k]]
           f acc (z,s) = ((head acc) + (k-1)*(acs!(z-k) + acs!z) - 2*s):acc             

