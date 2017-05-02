module Main where

import Data.Array
import Data.List
import qualified Data.Vector as V

main = do
     ls <- getContents
     let n:k:candies = map (\x -> read x :: Integer) $ lines ls
     putStrLn $ show $ getMin (sort candies) n k


getMin :: [Integer] -> Integer -> Integer -> Integer
getMin cs n k = first $ foldl f (initUnf, initUnf, initSum) [(k+1)..n]
     where acs = listArray (1, n) cs
           --initUnf = sum [acs!j - acs!i | i <- [1..k], j <- [(i+1)..k]]
           initUnf = unfairness acs k
           initSum = sum [acs!i | i <- [2..k]]
           f (minUnf,headUnf, s) z = (min curUnf minUnf, curUnf, s + acs!z - acs!(z-k+1))             
               where curUnf = (headUnf + (k-1)*(acs!(z-k) + acs!z) - 2*s)


first :: (Integer, Integer, Integer) -> Integer
first (x, _, _) = x

unfairness :: Array Integer Integer -> Integer -> Integer
unfairness a k = go k 0
     where go 1 sum = sum
           go x sum = go (x-1) (sum + a!x*(x-1) - a!(k-x+1)*(x-1))