module Main where

import System.Environment (getArgs)
import Data.List
import Data.Array
import Debug.Trace


main = do
    (file:_) <- getArgs
    contents <- readFile file
    let ls = map (\x -> (read (head $ words x) :: Int, read (last $ words x) :: Int)) (lines contents)
    let res = map (\(n,k) -> dropEggs n k) ls 
    mapM_ (print) res


dropEggs :: Int -> Int -> Int
dropEggs es fs = go es fs
     where go _ 0 = 0
           go 0 _ = 0
           go 1 k = k
           go n 1 = 1
           go n k = trace ("n: " ++ show n ++ ", k: " ++ show k) $ (+) 1 $ minimum $ map (\x -> maximum[eggs!(n-1,x-1), eggs!(n,k-x)]) [1..k]
           eggs = listArray bounds [go i j | (i, j) <- range bounds]
           bounds = ((0,0),(es,fs))