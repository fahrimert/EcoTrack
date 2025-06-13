import { UserDataMap } from "@/app/supervisor/superVizorDataTypes/types"
import { Table, TableHeader, TableRow, TableHead, TableBody, TableCell } from "@/components/ui/table"
import React from 'react'

export const LeaderBoardComponentForSupervizor = ({leaderTableFilter,dataforleaderboard} : {leaderTableFilter: string,dataforleaderboard: UserDataMap }) => {
 
  return (
    <>
    <h2>
{leaderTableFilter  == "Toplam Çözülen Sensör Tablosu" &&  " Toplam Çözülen Sensör Lider Tablosu" }
{leaderTableFilter  == "Süpervizörlerin Toplam Çözülmüş Task Tablosu" &&  "Süpervizörlerin Toplam Çözülmüş Task Tablosu" }
{leaderTableFilter  == "Süpervizörlerin Vermiş Oldukları Görevlerde Bugün Çözülen Görevler Tablosu" &&  "Süpervizörlerin Vermiş Oldukları Görevlerde Bugün Çözülen Görevler Tablosu" }


    </h2>
    <Table className="w-full">
      <TableHeader>
        <TableRow>
          <TableHead className="w-[40px] text-center">#</TableHead>
          <TableHead className="w-[200px]">İsmi</TableHead>
          <TableHead className="w-[100px]">{leaderTableFilter == "Supervizor Tasklerinin Ortalama Çözülme Tablosu"  && "Supervizor Tasklerinin Ortalama Çözülme Tablosu"  }

{leaderTableFilter  == "Süpervizörlerin Vermiş Oldukları Görevlerde Bugün Çözülen Görevler Tablosu" &&  "Süpervizörlerin Vermiş Oldukları Görevlerde Bugün Çözülen Görevler Tablosu" }

{leaderTableFilter == "Süpervizörlerin Toplam Çözülmüş Task Tablosu" && "Süpervizörlerin Toplam Çözülmüş Task Tablosu" }
          </TableHead>
        </TableRow >
      </TableHeader>
      <TableBody>
      {Object.entries(dataforleaderboard )
        .sort((a, b) => b[1] - a[1])
      .map (([key,value],z) => (
       <TableRow className="hover:bg-gray-200 ">
          <TableCell className="text-center font-medium">{z+1}</TableCell>
          <TableCell className="font-medium">
            <div className="flex items-center gap-2">
          
              <h2 className="text-blue-500 hover:underline" prefetch={false}>
            {key}       </h2>
            </div>
          </TableCell>
          <TableCell className="text-center">{value}</TableCell>
       
        </TableRow>
        ))}

      </TableBody>
    </Table>
    </>

  )

}
