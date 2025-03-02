package com.pxw.pojo.GA;


import com.pxw.pojo.Task;

//个体类  候选解
//负责存储 操作一条染色体
//两个构造方法  1.接受长度 随机初始化一条染色体  2.接受数组 作为染色体
public class Individual {
    //染色体
    private int[] chromsome;
    //适应度追踪
    private double fitness = -1;


    public Individual(int[] chromsome) {
        //创建个体染色体
        this.chromsome = chromsome;
    }

    //初始化   利用timetable对象来确定必须排课的班级数(染色体长度)
    //染色体本身由timetable随机取得教室，时段，教授生成
    public Individual(Bootstrap bootstrap){
        //班级数
        int numTables = bootstrap.getNumTables();

        //1位基因为房间   1位基因为时间段
        int chromosomeLength = numTables*2;
        //创建随机的染色体
        int[] newChromosome = new int[chromosomeLength];
        int chromosomeIndex = 0;

        //教学任务循环
        for (Task task : bootstrap.getTasksAsArray()){
            int timeslotId = -1;

            // 根据课程类型选择合适的起始节次,A=体育，B=实验课，其余为普通
//            System.out.println("task: " + task);
//            System.out.println("Cclasses: " + (task.getCclasses() != null ? task.getCclasses() : "null"));
//            System.out.println("Course: " + (task.getCclasses() != null ? task.getCclasses().getCourse() : "null"));

            if (task.requiresConsecutive()) {
                int consecutiveCount = task.getConsecutiveCount(); // 2或4
                int[] availableStartTimes = consecutiveCount == 2
                        ? new int[]{1, 3, 5, 7}
                        : new int[]{1, 3, 5};  // 2和4两种连排要求

                int startIndex = (int) (Math.random() * availableStartTimes.length);
                int startPeriod = availableStartTimes[startIndex];

                // 随机选择一个星期几（这里原来使用 (int)(Math.random()*5)+1 表示周一到周五）
                int day = (int) (Math.random() * 5) + 1;

                // 确保连排课程在同一天内：startPeriod + (consecutiveCount - 1) 不超过8
                if (startPeriod + consecutiveCount - 1 <= 8) {
                    timeslotId = (day - 1) * 8 + startPeriod;
                } else {
                    timeslotId = -1; // 无法选到合适的时间段
                }
            } else {
                // 非连排课程，随机选择时间段
                timeslotId = bootstrap.getRandomTimeslot().getId();
            }

            newChromosome[chromosomeIndex] = timeslotId;
            chromosomeIndex++;

            //加入随机房间
            int roomId = bootstrap.getRandomRoom().getId();
            newChromosome[chromosomeIndex] = roomId;
            chromosomeIndex++;

//                //加入教学任务
//                //加入随机时间
//                int timeslotId = bootstrap.getRandomTimeslot().getId();
//                newChromosome[chromosomeIndex] = timeslotId;
//                chromosomeIndex++;
//
//                //加入随机房间
//                int roomId = bootstrap.getRandomRoom().getId();
//                newChromosome[chromosomeIndex] = roomId;
//                chromosomeIndex++;

        }
        this.chromsome = newChromosome;

    }


    public int[] getChromsome() {
        return chromsome;
    }

    public int getChromsomeLength() {
        return this.chromsome.length;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }


    //基因
    public int getGene(int offset) {
        return this.chromsome[offset];
    }

    public void setGene(int offset, int gene) {
        this.chromsome[offset] = gene;
    }

    //输出基因字符串
    @Override
    public String toString() {
        String output = "";
        for (int gene = 0; gene < this.chromsome.length; gene++) {
            output += this.chromsome[gene];
        }

        return output;
    }
}
