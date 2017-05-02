module Main where

import Data.List
import qualified Data.Map as Map
import qualified Data.Set as Set
import Data.Maybe

main = do
    n <- getLine
    l <- getLine
    let xs = map read (words l)
    let sub = freq2elems $ freqs xs
    let f = show' $ concat $ iter $ map snd sub
    let res = if(isValid sub) then ("YES\n" ++ f) else "NO"
    putStrLn res


combine :: [Int] -> [Int] -> [Int]
combine mins cur = f mins set
     where f [] _ = []
           f (x:xs) s = x:(fromJust $ Set.lookupGT x s):f xs (Set.delete (fromJust $ Set.lookupGT x s) s)
           set = Set.fromList cur

freqs :: [Int] -> [(Int, Int)]
freqs xs = [(c, length g) | g@(c:_) <- group $ sort xs]

freq2elems :: [(Int, Int)] -> [(Int, [Int])]
freq2elems xs = Map.toDescList m
     where ins acc x = Map.insertWith (++) (snd x) ([fst x]) acc
           m = foldl ins Map.empty xs


isValid :: [(Int, [Int])] -> Bool
isValid sub = (ls == (take len (1:1:pt)))  && (reverse fs) == (take len [1..])
    where fs = map fst sub
          ls = map length $ map snd sub
          len = length fs
          pt = map (2^) [1..]

iter :: [[Int]] -> [[Int]]
iter (xs:[]) = [xs]
iter (xs:ys:[]) = xs:combine xs ys:[]
iter (xs:ys:rest) = xs: iter(combine xs ys:rest)

show' :: Show a => [a] -> String
show' = intercalate " " . map show
           