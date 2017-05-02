module Main where

import System.Environment (getArgs)
import Data.List


main = do
    (file:_) <- getArgs
    contents <- readFile file
    let ls = lines contents
    let cleanedLs = map parse ls
    let res = map (\x -> intercalate "," $ stringList (snd x) (fst x)) cleanedLs   
    mapM_ (putStrLn) res



stringList :: String -> Int -> [String]
stringList s 0 = [""]
stringList s n = [ c : sub | c <- s, sub <- subs]
    where subs = stringList s (n-1)

rmdups :: (Ord a) => [a] -> [a]
rmdups = map head . group . sort

replace :: (Eq a) => a -> a -> [a] -> [a]
replace toRep repWith = map (\x -> if(x == toRep) then repWith else x)

split :: Char -> String -> [String]
split delim s = words $ replace delim ' ' s


parse :: String -> (Int, String)
parse str = (read (splitted !! 0) :: Int, rmdups $ splitted !! 1)
    where splitted = split ',' str
          