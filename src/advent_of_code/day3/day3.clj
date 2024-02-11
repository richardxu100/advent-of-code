(ns advent-of-code.day3.day3)

(defn is-digit?
  "Check if the given character represents a digit."
  [char]
  (try
    (Integer/parseInt (str char))
    true
    (catch NumberFormatException _
      false)))

(defn is-symbol?
  "docstring"
  [char]
  (not (or (is-digit? char) (= "." char))))

(defn is-in-graph?
  "docstring"
  [[x y] graph]
  (let [graph-length (count (first graph))
        graph-height (count graph)]
    (and (> graph-length x) (> graph-height y) (not (neg-int? x)) (not (neg-int? y)))))

(defn get-graph-value
  "docstring"
  [graph [x y]]
  (nth (nth graph y) x))

(defn is-valid-num
  "docstring"
  [current-num current-num-index y graph]
  (let [left-neighbor [(dec current-num-index) y]
        right-neighbor [(+ current-num-index (count current-num)) y]
        down-neighbor [current-num-index (inc y)]]  ; add more tests, change to all-neighbors or something. tdd can be weird
    (or (and (is-in-graph? left-neighbor graph) (is-symbol? (get-graph-value graph left-neighbor)))
        (and (is-in-graph? right-neighbor graph) (is-symbol? (get-graph-value graph right-neighbor)))
        (and (is-in-graph? down-neighbor graph) (is-symbol? (get-graph-value graph down-neighbor))))))

(defn find-valid-nums
  "docstring"
  [graph row y]
  (loop [index 0
         remaining row
         current-num ""
         current-num-index nil
         valid-nums []]
    (cond
      (empty? remaining)
      (if (empty? current-num)
        (apply + valid-nums)
        (if (is-valid-num current-num current-num-index y graph)
          (let [all-valid-nums (conj valid-nums (Integer/parseInt current-num))]
            (apply + all-valid-nums))
          (apply + valid-nums)))
      (is-digit? (first remaining))
      (recur (inc index) (rest remaining) (str current-num (first remaining)) (if (nil? current-num-index)
                                                                                index
                                                                                current-num-index) valid-nums)
      (not (empty? current-num))
      (recur (inc index) (rest remaining) "" nil (if (is-valid-num current-num current-num-index y graph)
                                                   (conj valid-nums (Integer/parseInt current-num))
                                                   valid-nums))
      :else
      (recur (inc index) (rest remaining) current-num current-num-index valid-nums))))

(defn sum-of-parts
  "docstring"
  [graph]
  (apply + (map-indexed (fn [y row] (find-valid-nums graph row y)) graph)))