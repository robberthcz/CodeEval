module Main where

import System.Environment (getArgs)
--import Data.List
import Debug.Trace
import Data.Set as Set

main = do
    conts <- getContents
    let seq = Prelude.map (\x -> read x :: Int) $ drop 1 $ lines conts
    putStrLn (show $ lis2 seq)

lis2 :: [Int] -> Int
lis2 xs = Set.size (Prelude.foldl f Set.empty xs)
    where f set num = case Set.lookupGE num set of
                            Nothing -> Set.insert num set
                            Just a -> Set.insert num (Set.delete a set) 



{- main = do
    conts <- getContents
    let seq = map (\x -> read x :: Int) $ drop 1 $ lines conts
    putStrLn (show $ length $ lis seq)

lis :: [Int] -> [Int]
lis xs = reverse $ lis' [([], 0)] xs

lis' :: [([Int], Int)] -> [Int] -> [Int]
lis' l    [] = fst $  maximumBy tcmp l
lis' [([],0)] (s:ss)  = lis' [([s], 1)]   ss
lis' l    (s:ss)  = lis' (thisSeq : l) ss
  where 
    seqs =  filter (\x -> s > (head $ fst x)) l
    maxSeq = if(length seqs == 0) then ([],0) else maximumBy tcmp seqs
    thisSeq = (s:(fst maxSeq), (snd maxSeq) + 1)

tcmp = \x y -> compare (snd x) (snd y) -}
