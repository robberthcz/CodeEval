module Main where

import Data.List
import Data.Ord

main = do
    l1 <- getLine
    l2 <- getLine
    let [n,k] = toIntList l1
    let fs = splitEvery k $ sortBy (comparing Down) $ toIntList l2
    let res = sum $ zipWith (\xs y -> sum $ map ((*) y) xs) fs [1..]
    putStrLn $ show res



toIntList :: String -> [Int]
toIntList str = map (\x -> read x :: Int) $ words str

splitEvery :: Int -> [a] -> [[a]]
splitEvery _ [] = []
splitEvery n xs = as : splitEvery n bs 
     where (as,bs) = splitAt n xs