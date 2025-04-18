(ns advent-of-code-2024.day3.day3)

(def mult_pattern #"mul\((\d+),(\d+)\)")
(def do-pattern #"do\(\)")
(def dont-pattern #"don't\(\)")

(def real-input (slurp "Desktop/code/clojure/advent-of-code/src/advent_of_code_2024/day3/day3_input.txt"))

(defn find-multiplication-strings [input]
  (re-seq mult_pattern input))

(defn calculate-product [[_, x, y]]
  (* (parse-long x) (parse-long y)))

(defn part1 [input]
  (->> input
       find-multiplication-strings
       (map calculate-product)
       (apply +)))

(part1 real-input)

(defn is-tracked-phrase? [phrase]
  (or (seq (find-multiplication-strings phrase))
      (or (= phrase "do()") (= phrase "don't()"))))

;; Algorithm
;; Can find all indices of the multiplications. Find all indices of do () and don't () these won't overalp
;; Then determine the order of these commands, by their indices, and then represent the ordering in a list
;; Parse the list, to determine which multiplications to apply
; (def mult-matcher (re-matcher mult_pattern real-input))
(defn find-indices-for-multiplication [input]
  (let [mult-matcher (re-matcher mult_pattern input)]
    (loop [matches []]
      (if (.find mult-matcher)
        (let [start (.start mult-matcher)
              end (.end mult-matcher)
              full (.group mult-matcher 0)
              x (.group mult-matcher 1)
              y (.group mult-matcher 2)]
          (recur (conj matches {:type :mult, :match full :x (Integer/parseInt x) :y (Integer/parseInt y) :index start :end end})))
        matches))));

(find-indices-for-multiplication real-input)

(defn find-indices-for-do [input]
  (let [do-matcher (re-matcher do-pattern input)]
    (loop [matches []]
      (if (.find do-matcher)
        (let [start (.start do-matcher)
              end (.end do-matcher)
              full (.group do-matcher 0)]
          (recur (conj matches {:type :do, :match full :index start :end end})))
        matches))));

(defn find-indices-for-dont [input]
  (let [dont-matcher (re-matcher dont-pattern input)]
    (loop [matches []]
      (if (.find dont-matcher)
        (let [start (.start dont-matcher)
              end (.end dont-matcher)
              full (.group dont-matcher 0)]
          (recur (conj matches {:type :dont, :match full :index start :end end})))
        matches))));

(find-indices-for-do real-input)
(find-indices-for-dont real-input)

(defn get-first-el-index [l]
  "Returns the index of the first element. Max int if empty list"
  (if (empty? l)
    Integer/MAX_VALUE
    (:index (first l))))

(defn find-lowest-index [remaining-do remaining-dont remaining-mult]
  (let [do-index (get-first-el-index remaining-do)
        dont-index (get-first-el-index remaining-dont)
        remaining-mult-index (get-first-el-index remaining-mult)
        min-index (min do-index dont-index remaining-mult-index)]
    (condp = min-index
      do-index :do
      dont-index :dont
      remaining-mult-index :mult)))

(find-lowest-index [{:index 5}] [{:index 11}] [{:index 31}])

(defn generate-ordered-instructions [input]
  (loop [remaining-do (find-indices-for-do input)
         remaining-dont (find-indices-for-dont input)
         remaining-mult (find-indices-for-multiplication input)
         ordered-instructions []]
    (if (every? empty? [remaining-do, remaining-dont, remaining-mult])
      ordered-instructions
      (let [lowest-index (find-lowest-index remaining-do remaining-dont remaining-mult)]
        (case lowest-index
          :do (recur (rest remaining-do) remaining-dont remaining-mult (conj ordered-instructions (first remaining-do)))
          :dont (recur remaining-do (rest remaining-dont) remaining-mult (conj ordered-instructions (first remaining-dont)))
          :mult (recur remaining-do remaining-dont (rest remaining-mult) (conj ordered-instructions (first remaining-mult))))))))

(def ordered-instructions (generate-ordered-instructions real-input))

(defn sum-enabled-instructions [ordered-instructions]
  (loop [remaining-instructions ordered-instructions
         allow-multiplication true
         total 0]
    (if (empty? remaining-instructions)
      total
      (let [top-instruction (first remaining-instructions)]
        (case (:type top-instruction)
          :do
          (recur (rest remaining-instructions) true total)
          :dont
          (recur (rest remaining-instructions) false total)
          :mult
          (recur (rest remaining-instructions) allow-multiplication
                 (if (not allow-multiplication)
                   total
                   (+ total (* (:x top-instruction) (:y top-instruction))))))))))

; I've got a lot to learn about reduce. It's a nice way to avoid loop recur, which isn't the easiest thing to read
(defn sum-enabled-instructions-with-reduce [ordered-instructions]
  (let [[total _]
        (reduce (fn [[total allow-mult] {:keys [type] :as instr}]
                  (case type
                    :do [total true]
                    :dont [total false]
                    :mult (if allow-mult
                            (let [{:keys [x y]} instr]
                              [(+ total (* x y)) allow-mult])
                            [total allow-mult])))
                [0 true]
                ordered-instructions)]
    total))

(take 15 ordered-instructions)
(sum-enabled-instructions ordered-instructions)
(sum-enabled-instructions-with-reduce ordered-instructions)

(filter #(= (:type %) :do) ordered-instructions)

; correct!! : 

(reduce + [1 2 3 4 5])
; the other is just a fancy reduce that works on maps, and returns two values in the recursive loop. Also destructures each individual map using that weird syntax

; Also play around with the clojure pattern matching library









