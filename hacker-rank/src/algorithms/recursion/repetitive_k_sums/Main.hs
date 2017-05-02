module Main where

import qualified Data.Map as Map


main = do
    l <- getLine
    let t = read l :: Int
    go t
        where go 0 = return ()
              go s = do
                  l1 <- getLine
                  l2 <- getLine
                  let [n,k] = map (\x -> read x :: Int) $ words l1
                  let ksums = map (\x -> read x :: Int) $ words l2
                  let mapF = foldl addFreq Map.empty ksums
                  let (minSum, _) = Map.findMin mapF
                  let minElem = (div) minSum k
                  putStrLn $ unwords $ reverse $ map show $ getSeq (deleteFreq mapF minSum) (minElem:[]) k n minElem
                  go (s-1)



addFreq :: (Ord a) => Map.Map a Int -> a -> Map.Map a Int
addFreq map key = Map.insertWith (+) key 1 map

deleteFreq :: (Ord a) => Map.Map a Int -> a -> Map.Map a Int
deleteFreq map key = case Map.lookup key map of
                         Nothing -> map
                         Just 1 -> Map.delete key map
                         Just v -> Map.adjust (flip (-) 1) key map


sumCombs :: (Int, Int) -> Int -> [Int]
sumCombs (x,y) k = go (x*k) (k+1)
     where go s 0 = []
           go s n = s:(go (s-x+y) (n-1))

getSeq :: Map.Map Int Int -> [Int] -> Int -> Int -> Int -> [Int]
getSeq map seq k n min | ((length seq) == n) || (Map.null map) = seq
                       | otherwise = getSeq res (nextElem:seq) k n min
                                       where (minSum, _) = Map.findMin map
                                             nextElem = minSum - k*min + min
                                             combs = concat [sumCombs(nextElem, s) k | s <- seq]
                                             res = foldl deleteFreq map combs