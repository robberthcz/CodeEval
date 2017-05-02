module Main where


main = do
      l <- getLine
      let [n, m] = map (\x -> read x :: Int) $ words l
      putStrLn $ show $ picks (n*m)

picks :: Int -> Double
picks n = 1 + (fromIntegral n)*(harmonicSum (n-1))

harmonicSum :: Int -> Double
harmonicSum 0 = 0
harmonicSum n = go n 0.00
     where go 1 sum = sum + 1
           go n sum = go (n-1) (sum + 1/(fromIntegral n))