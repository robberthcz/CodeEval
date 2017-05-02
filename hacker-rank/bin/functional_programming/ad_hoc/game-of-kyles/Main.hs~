module Main where

import Control.Applicative
import System.Environment (getArgs)

main = do
    tc <- (read :: String -> Int) <$> getLine
    let res = sierpinski tc
    putStrLn (unlines res)
    

triangle :: Int -> Int -> [[Char]]
triangle h w = foldl f [] [0..(h-1)]
     where f acc v = (unders ++ ones ++ unders):acc
               where unders = replicate v '_'
                     ones = replicate (w - 2*v) '1'


sierpinski :: Int -> [[Char]]
sierpinski n = sierpinski' n 32 63
     where sierpinski' 0 h w = triangle h w
           sierpinski' n h w = (zipWith3 (+++) sidefill t sidefill) ++ (zipWith3 (+++) t midfill t)
               where t = sierpinski' (n-1) (h `div` 2) (w `div` 2)
                     midfill = map (\x -> [x]) (repeat '_')
                     sidefill = repeat (replicate ((w+1) `div` 4 ) '_')
                     (+++) = \x y z -> x ++ y ++ z