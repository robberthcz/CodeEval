module Main where

import Data.Array

main = do
        l1 <- getLine
        l2 <- getLine
        let n = read ((words l1) !! 0) :: Int
        let coins = map (\x -> read x :: Int) (words l2)
        putStrLn $ show $ coinChange n coins
       
coinChange :: Int -> [Int] -> Int
coinChange n coins = go (length coins - 1) n
    where m = length coins
          aS = listArray (0, (length coins) - 1) coins
          bounds = ((0,0),(m,n))
          accessTable id1 id2 = if(id1 < 0 || id2 < 0) then 0 else dp ! (id1, id2)
          go _ 0 = 1
          go i j = accessTable (i-1) j + accessTable i (j - (aS ! i))
          dp = listArray bounds [go i j | (i, j) <- range bounds]
