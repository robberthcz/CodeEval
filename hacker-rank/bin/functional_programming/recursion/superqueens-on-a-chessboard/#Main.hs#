module Main where

import Control.Applicative
import System.Environment (getArgs)

main = do
    tc <- (read :: String -> Int) <$> getLine
    let res = length $ queens tc
    putStrLn (show res)
    

queens :: Int -> [[Int]]
queens n = queens' n n
     where queens' _ 0 = [[]]
           queens' n k = [row : qs | qs <- queens' n (k-1), row <- [1..n], isSafe row qs]
          

isSafe :: Int -> [Int] -> Bool
isSafe row qs = not(sameRow) && not(onDiagonal) && not(byK)
    where sameRow = any (row==) qs
          qs' = zip (map (\x -> abs(row-x)) qs) [1..]
          onDiagonal = any (\x -> fst x == snd x) qs'
          byK = byKnight qs'

byKnight :: [(Int, Int)] -> Bool
byKnight [] = False
byKnight ((r1,c1):[]) =         r1 == 2 
byKnight ((r1,c1):(r2,c2):_) = (r1 == 2) || (r2 == 1)