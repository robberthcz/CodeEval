module Main where

import Control.Applicative
import System.Environment (getArgs)
import Data.List

main = do
    conts <- getContents
    let ls = lines conts
    let m = read ( ls !! 0) :: Int
    let len = 2 ^ m
    let nums = split ' ' (ls !! 1)
    let row = read (nums !! 0) :: Int
    let col = read (nums !! 1) :: Int
    let trominoes = troms ((1,1),(len, len)) (row, col)
    mapM_ (putStrLn . trom2str) trominoes

split :: Eq a => a -> [a] -> [[a]]
split d [] = []
split d s = x : split d (drop 1 y) where (x,y) = span (/= d) s
    
    
type Pos = (Int, Int)
type Board = (Pos, Pos)
type Trom = (Pos, Pos, Pos)

trom2str :: Trom -> String
trom2str ((r1,c1),(r2,c2),(r3,c3)) = show r1 ++ " " ++ show c1 ++ " " ++ show r2 ++ " " ++ show c2 ++ " " ++ show r3 ++ " " ++ show c3

troms :: Board -> Pos -> [Trom]
troms b pos
  | fst b == snd b = []
  | boardContains leftUp pos   = (ruC, ldC, rdC) : ((troms leftUp pos) ++ (troms leftDown ldC) ++ (troms rightUp ruC) ++ (troms rightDown rdC))
  | boardContains leftDown pos = (luC, ruC, rdC) : ((troms leftUp luC) ++ (troms leftDown pos) ++ (troms rightUp ruC) ++ (troms rightDown rdC))
  | boardContains rightUp pos  = (luC, ldC, rdC) : ((troms leftUp luC) ++ (troms leftDown ldC) ++ (troms rightUp pos) ++ (troms rightDown rdC))
  | otherwise                  = (luC, ruC, ldC) : ((troms leftUp luC) ++ (troms leftDown ldC) ++ (troms rightUp ruC) ++ (troms rightDown pos))
  where
    (leftUp, leftDown, rightUp, rightDown) = divideBoard b
    (luC, ldC, ruC, rdC) = (snd leftUp, shiftPos(snd leftUp) (1,0), shiftPos(snd leftUp) (0,1),  shiftPos(snd leftUp) (1,1))

divideBoard :: Board -> (Board, Board, Board, Board)
divideBoard (down, up) = (leftUp, leftDown, rightUp, rightDown)
  where
    shift = (((fst up) - (fst down) + 1) `div` 2) - 1
    leftUp    = (down, shiftPos down (shift, shift))
    leftDown  = shiftBoard leftUp (shift + 1,0)
    rightUp   = shiftBoard leftUp (0,shift + 1)
    rightDown = shiftBoard leftUp (shift + 1,shift + 1)

shiftPos :: Pos -> Pos -> Pos
shiftPos from by = (fst from + fst by, snd from + snd by)

shiftBoard :: Board -> Pos -> Board
shiftBoard (down, up) pos = (shiftPos down pos, shiftPos up pos)

boardContains :: Board -> Pos -> Bool
boardContains (down, up) (row, col) = (downRow <= row) && (downCol <= col) && (row <= upRow) && (col <= upCol)
  where
    downRow = fst down
    downCol = snd down
    upRow = fst up
    upCol = snd up
