module Main where

import Control.Applicative
import System.Environment (getArgs)
import Data.List

data Subg = WIN | LOSE deriving Show


main = do
    conts <- getContents
    let tcs = each 2 $ drop 1 $ lines conts
    let res = map(show . solve . parseGame) tcs
    putStrLn (unlines res)
    

parseGame :: String -> [Subg]
parseGame = map (\x -> if(x `mod` 3 == 0) then LOSE else WIN) .  map length . filter ((=='I') . head) . groupBy (==)

each n [] = []
each n xs = (head xss) : each n (drop n xs)
    where xss = drop (n-1) xs 

solve :: [Subg] -> Subg
solve [] = error "Cannot pass empty list"
solve (x:[]) = x
solve (WIN:LOSE:subgs) = solve(WIN:subgs)
solve (LOSE:WIN:subgs) = solve(WIN:subgs)
solve (_:_:subgs) = solve(LOSE:subgs)

