module Main where

import Data.List
import Data.Ord

main = do
     l <- getLine
     let ts = read l :: Int
     go ts
         where go 0 = return ()
               go t = do
                   l1 <- getLine
                   l2 <- getLine
                   let n = read l1 :: Integer
                   --let hps = scanl1 (+) $ reverse $ sort $ map (\x -> read x :: Int) $ words l2
                   --let maxXp = maximum $ zipWith (*) hps [n,(n-1)..1]
                   --putStrLn $ show maxXp
                   let hps = sortBy (comparing Down) $ map (\x -> read x :: Integer) $ words l2
                   putStrLn $ show $ garnXp hps n
                   go (t-1)

garnXp :: [Integer] -> Integer -> Integer
garnXp xs n = garnXp' xs (n,0,0)
    where garnXp' [] (_,_,curMax) = curMax
          garnXp' ls (s,curCumSum,curMax) = if(newMax <= curMax) then curMax else garnXp' (tail ls) (s-1,cumSum,newMax)
              where hp = head ls
                    cumSum = curCumSum + hp
                    newMax = cumSum*s


