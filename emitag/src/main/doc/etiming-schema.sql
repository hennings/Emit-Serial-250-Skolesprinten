USE [VMTest2016Test1_2016]
GO
/****** Object:  Table [dbo].[arr]    Script Date: 20.06.2018 00:04:40 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[arr](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[arr] [char](3) NULL,
	[SUB] [char](3) NULL,
	[YEAR] [int] NULL,
	[NAME] [char](32) NULL,
	[eventplace] [char](32) NULL,
	[Organizator] [char](32) NULL,
	[firststart] [datetime] NULL,
	[ADDRESS] [char](30) NULL,
	[POST] [char](5) NULL,
	[PLACE] [char](15) NULL,
	[NATION] [char](4) NULL,
	[BANK] [char](13) NULL,
	[PNR] [int] NULL,
	[KID] [int] NULL,
	[seeding] [bit] NULL,
	[pmalt] [bit] NULL,
	[TIMINGNO] [int] NULL,
	[addmin] [bit] NULL,
	[TIDEL] [int] NULL,
	[POINTS] [int] NULL,
	[karusell] [bit] NULL,
	[ecards] [bit] NULL,
	[ENTRYNO] [int] NULL,
	[entrynoon] [bit] NULL,
	[addshow] [bit] NULL,
	[ADDTXT] [char](15) NULL,
	[ADDTXT2] [char](15) NULL,
	[sortorder] [bit] NULL,
	[BOLPLACE] [int] NULL,
	[boldate] [bit] NULL,
	[AUTOSTART] [int] NULL,
	[AUTOTID] [int] NULL,
	[printraces] [bit] NULL,
	[bollanes] [bit] NULL,
	[2xecard] [bit] NULL,
	[bolarr] [bit] NULL,
	[BOLPOINTS] [int] NULL,
	[boloffset] [bit] NULL,
	[bolclass] [bit] NULL,
	[LAPCONTROL] [int] NULL,
	[finishontrol] [int] NULL,
	[bolhistory] [bit] NULL,
	[bolbof] [bit] NULL,
	[kidon] [bit] NULL,
	[feeon] [bit] NULL,
	[kmon] [bit] NULL,
	[lisenson] [bit] NULL,
	[landon] [bit] NULL,
	[lando] [bit] NULL,
	[payon] [bit] NULL,
	[regon] [bit] NULL,
	[pregroup] [bit] NULL,
	[DISIPLIN] [char](2) NULL,
	[addpen] [bit] NULL,
	[nm] [bit] NULL,
	[SKIVER] [int] NULL,
	[HTMLBILDE] [char](60) NULL,
	[HTMLLINK] [char](60) NULL,
	[htmlheader] [bit] NULL,
	[ettminutt] [bit] NULL,
	[sperrsiste] [bit] NULL,
	[uselaps] [bit] NULL,
	[emitdisiplin] [char](5) NULL,
	[EMITCODEX] [int] NULL,
	[startdelay] [bit] NULL,
	[STARTDELAYSEC] [int] NULL,
	[FEDERATION] [char](32) NULL,
	[emitgren] [char](5) NULL,
	[VHEADER] [char](60) NULL,
	[HHEADER] [char](60) NULL,
	[BOTTOM] [char](60) NULL,
	[toppen] [char](60) NULL,
	[emitbrikke] [int] NULL,
	[resdesimal] [int] NULL,
 CONSTRAINT [arrprim] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[class]    Script Date: 20.06.2018 00:04:40 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[class](
	[code] [varchar](32) NOT NULL,
	[class] [varchar](32) NULL,
	[cource] [int] NULL,
	[firststart] [float] NULL,
	[entryfee1] [money] NULL,
	[entryfee2] [money] NULL,
	[entryfee3] [money] NULL,
	[fromstartno] [int] NULL,
	[tostartno] [int] NULL,
	[order] [int] NULL,
	[sex] [char](1) NULL,
	[timingtype] [int] NULL,
	[numbers] [int] NULL,
	[intervall] [int] NULL,
	[vacant] [int] NULL,
	[cheaseing] [bit] NULL,
	[ecardfee] [money] NULL,
	[fork] [int] NULL,
	[lastprint] [datetime] NULL,
	[fromyear] [int] NULL,
	[toyear] [int] NULL,
	[price] [int] NULL,
	[tag] [char](15) NULL,
	[ready] [bit] NULL,
	[lisensfee] [money] NULL,
	[purrule] [int] NULL,
	[purmin] [int] NULL,
	[pointsrule] [int] NULL,
	[ptid] [datetime] NULL,
	[direct] [bit] NULL,
	[classrank] [bit] NULL,
	[entryid] [int] NULL,
	[elite] [bit] NULL,
	[n3name] [char](10) NULL,
	[mtider] [int] NULL,
	[freestart] [bit] NULL,
	[nomultires] [bit] NULL,
	[codex] [int] NULL,
	[displaytype] [int] NULL,
	[Exercise] [char](32) NULL,
	[catid] [int] NULL,
	[sector] [char](2) NULL,
	[penalty] [int] NULL,
	[showrule] [int] NULL,
	[pursuitlaps] [int] NULL,
	[begrens] [int] NULL,
	[fasteskiver] [bit] NULL,
	[rule] [int] NULL,
	[finalecodex] [int] NULL,
	[sprintrule] [int] NULL,
	[etappenavn] [bit] NULL,
	[pursuitgruppe] [int] NULL,
	[firstIsStart] [bit] NULL,
	[noStartno] [bit] NULL,
	[isTeamsprint] [bit] NULL,
	[eqclass] [char](32) NULL,
	[eqclassRaceUid] [int] NULL,
	[fiscategory] [varchar](4) NULL,
	[fisevent] [varchar](3) NULL,
	[sortorder] [int] NULL,
 CONSTRAINT [classprim] PRIMARY KEY CLUSTERED 
(
	[code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[ecard]    Script Date: 20.06.2018 00:04:40 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ecard](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[ecardno] [int] NULL,
	[control] [int] NULL,
	[times] [int] NULL,
	[orgtimes] [int] NULL,
	[nr] [int] NULL,
	[timechanged] [float] NULL,
	[clocktime] [float] NULL,
 CONSTRAINT [ecardprim] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[mellom]    Script Date: 20.06.2018 00:04:40 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[mellom](
	[mellomid] [int] IDENTITY(1,1) NOT NULL,
	[id] [int] NULL,
	[mtime] [float] NULL,
	[strtid] [char](12) NULL,
	[iplace] [int] NULL,
	[mintime] [float] NULL,
	[mchanged] [float] NULL,
	[stasjon] [int] NULL,
	[rundenr] [int] NULL,
	[serial] [int] NULL,
	[message] [int] NULL,
	[day] [int] NULL,
	[nettotid] [float] NULL,
	[mecard] [int] NULL,
	[timechanged] [float] NULL,
	[ServerID] [int] NULL,
	[ServerType] [int] NULL,
	[mExclude] [bit] NULL,
	[isSendt] [bit] NULL,
 CONSTRAINT [mellomid] PRIMARY KEY CLUSTERED 
(
	[mellomid] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[name]    Script Date: 20.06.2018 00:04:40 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[name](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[kid] [char](13) NULL,
	[ename] [char](32) NULL,
	[name] [varchar](32) NULL,
	[sex] [char](1) NULL,
	[nation] [char](5) NULL,
	[arr] [char](3) NULL,
	[sub] [char](3) NULL,
	[years] [int] NULL,
	[startno] [int] NULL,
	[times] [char](12) NULL,
	[place] [int] NULL,
	[team] [varchar](10) NULL,
	[class] [varchar](32) NULL,
	[cource] [int] NULL,
	[starttime] [float] NULL,
	[intime] [float] NULL,
	[ecard] [int] NULL,
	[pnr] [int] NULL,
	[ptype] [int] NULL,
	[status] [char](1) NULL,
	[seed] [int] NULL,
	[ecardfee] [bit] NULL,
	[tag] [char](10) NULL,
	[changed] [datetime] NULL,
	[changedby] [char](10) NULL,
	[feelevel] [int] NULL,
	[seeding] [int] NULL,
	[miltimes] [int] NULL,
	[readtime] [float] NULL,
	[totaltime] [float] NULL,
	[races] [int] NULL,
	[tag1] [char](60) NULL,
	[cp] [bit] NULL,
	[info] [char](15) NULL,
	[contact] [int] NULL,
	[rank] [int] NULL,
	[lisens] [char](15) NULL,
	[lisensfee] [bit] NULL,
	[payd] [bit] NULL,
	[timetype] [float] NULL,
	[points] [float] NULL,
	[fodt] [datetime] NULL,
	[ecard2] [int] NULL,
	[prewarning] [bit] NULL,
	[rounds] [int] NULL,
	[training] [int] NULL,
	[gruppe] [char](15) NULL,
	[distrikt] [char](5) NULL,
	[fispoints] [float] NULL,
	[timernr] [int] NULL,
	[heat] [int] NULL,
	[ch] [bit] NULL,
	[timechanged] [float] NULL,
	[adr1] [char](32) NULL,
	[adr2] [char](32) NULL,
	[postnr] [char](10) NULL,
	[poststed] [char](32) NULL,
	[land] [char](4) NULL,
	[tlf] [char](15) NULL,
	[email] [char](50) NULL,
	[pdato] [datetime] NULL,
	[fno] [char](10) NULL,
	[div1] [bit] NULL,
	[div2] [bit] NULL,
	[div3] [bit] NULL,
	[div4] [bit] NULL,
	[statusmsg] [char](35) NULL,
	[div5] [bit] NULL,
	[div6] [bit] NULL,
	[div7] [bit] NULL,
	[div8] [bit] NULL,
	[klubbnavn] [char](40) NULL,
	[lnr] [int] NULL,
	[paralell] [int] NULL,
	[dsqver] [bit] NULL,
	[ff] [bit] NULL,
	[felttid] [float] NULL,
	[pursuit] [int] NULL,
	[birkid] [char](32) NULL,
	[addtime] [float] NULL,
	[intime2] [float] NULL,
	[prosent] [int] NULL,
	[eqref] [int] NULL,
	[start2] [float] NULL,
	[ecard3] [int] NULL,
	[ecard4] [int] NULL,
	[isfissend] [int] NULL,
 CONSTRAINT [namePrimary] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[radiopost]    Script Date: 20.06.2018 00:04:40 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[radiopost](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[code] [int] NULL,
	[finish] [bit] NULL,
	[html] [bit] NULL,
	[speaker] [bit] NULL,
	[live] [bit] NULL,
	[radiocourceno] [int] NULL,
	[radiorundenr] [int] NULL,
	[eqref] [int] NULL,
	[radiodist] [int] NULL,
	[radiotype] [int] NULL,
	[etappe] [int] NULL,
	[stedsnavn] [char](32) NULL,
	[description] [char](32) NULL,
	[radioday] [int] NULL,
	[show] [bit] NULL,
	[timingpointtype] [char](4) NULL,
	[radioorder] [int] NULL,
 CONSTRAINT [radiopostprim] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[relay]    Script Date: 20.06.2018 00:04:40 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[relay](
	[lagno] [int] IDENTITY(1,1) NOT NULL,
	[lgstartno] [int] NULL,
	[teamno] [int] NULL,
	[lgteam] [char](32) NULL,
	[lgclass] [char](32) NULL,
	[lgplace] [int] NULL,
	[lgtotaltime] [float] NULL,
	[lglegno] [int] NULL,
	[lgcommon] [int] NULL,
	[lgstatus] [char](1) NULL,
	[lgtag] [int] NULL,
	[lgniva] [int] NULL,
	[lgtmpnr] [int] NULL,
	[lgbetalt] [bit] NULL,
	[lgaddtime] [float] NULL,
	[lgheat] [int] NULL,
	[lgchecked] [bit] NULL,
	[lgorder] [int] NULL,
	[remarks] [char](255) NULL,
	[timechanged] [float] NULL,
	[lgheatstart] [float] NULL,
	[lgfispoints] [float] NULL,
	[lgsemiplass] [int] NULL,
	[lgsemitid] [float] NULL,
 CONSTRAINT [lagnoprim] PRIMARY KEY CLUSTERED 
(
	[lagno] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[status]    Script Date: 20.06.2018 00:04:40 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[status](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[code] [char](1) NULL,
	[namestr] [char](32) NULL,
	[shortname] [char](3) NULL,
	[gru] [int] NULL,
 CONSTRAINT [statusprim] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[team]    Script Date: 20.06.2018 00:04:40 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[team](
	[code] [varchar](10) NOT NULL,
	[name] [varchar](32) NULL,
	[adr1] [char](32) NULL,
	[adr2] [char](32) NULL,
	[adr3] [char](32) NULL,
	[ponr] [char](6) NULL,
	[place] [char](30) NULL,
	[nation] [char](3) NULL,
	[telephone] [char](20) NULL,
	[email] [char](50) NULL,
	[tag1] [char](2) NULL,
	[kid] [char](12) NULL,
	[payd] [money] NULL,
	[fias] [char](12) NULL,
	[district] [int] NULL,
	[tag2] [smallint] NULL,
	[reslist] [int] NULL,
	[res1] [int] NULL,
	[res2] [int] NULL,
	[res3] [int] NULL,
	[res4] [int] NULL,
	[res5] [int] NULL,
	[res6] [int] NULL,
	[res7] [int] NULL,
	[res8] [int] NULL,
	[konto] [char](13) NULL,
	[tel2] [char](20) NULL,
	[mob] [char](20) NULL,
 CONSTRAINT [teamprim] PRIMARY KEY CLUSTERED 
(
	[code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [seeding]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [pmalt]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [addmin]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [karusell]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((1)) FOR [ecards]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [entrynoon]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [addshow]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [sortorder]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((1)) FOR [boldate]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [printraces]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [bollanes]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [2xecard]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [bolarr]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [boloffset]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [bolclass]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [bolhistory]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [bolbof]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [kidon]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [feeon]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [kmon]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [lisenson]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [landon]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [lando]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((1)) FOR [payon]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [regon]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [pregroup]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [addpen]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [nm]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [htmlheader]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((1)) FOR [ettminutt]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [sperrsiste]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [uselaps]
GO
ALTER TABLE [dbo].[arr] ADD  DEFAULT ((0)) FOR [startdelay]
GO
ALTER TABLE [dbo].[class] ADD  DEFAULT ((0)) FOR [cheaseing]
GO
ALTER TABLE [dbo].[class] ADD  DEFAULT ((0)) FOR [ready]
GO
ALTER TABLE [dbo].[class] ADD  DEFAULT ((0)) FOR [direct]
GO
ALTER TABLE [dbo].[class] ADD  DEFAULT ((0)) FOR [classrank]
GO
ALTER TABLE [dbo].[class] ADD  DEFAULT ((0)) FOR [elite]
GO
ALTER TABLE [dbo].[class] ADD  DEFAULT ((0)) FOR [freestart]
GO
ALTER TABLE [dbo].[class] ADD  DEFAULT ((0)) FOR [nomultires]
GO
ALTER TABLE [dbo].[class] ADD  DEFAULT ((0)) FOR [fasteskiver]
GO
ALTER TABLE [dbo].[class] ADD  DEFAULT ((0)) FOR [etappenavn]
GO
ALTER TABLE [dbo].[class] ADD  DEFAULT ((0)) FOR [firstIsStart]
GO
ALTER TABLE [dbo].[class] ADD  DEFAULT ((0)) FOR [noStartno]
GO
ALTER TABLE [dbo].[class] ADD  DEFAULT ((0)) FOR [isTeamsprint]
GO
ALTER TABLE [dbo].[mellom] ADD  DEFAULT ((1)) FOR [mExclude]
GO
ALTER TABLE [dbo].[mellom] ADD  DEFAULT ((1)) FOR [isSendt]
GO
ALTER TABLE [dbo].[name] ADD  DEFAULT ((0)) FOR [ecardfee]
GO
ALTER TABLE [dbo].[name] ADD  DEFAULT ((0)) FOR [cp]
GO
ALTER TABLE [dbo].[name] ADD  DEFAULT ((0)) FOR [lisensfee]
GO
ALTER TABLE [dbo].[name] ADD  DEFAULT ((0)) FOR [payd]
GO
ALTER TABLE [dbo].[name] ADD  DEFAULT ((0)) FOR [prewarning]
GO
ALTER TABLE [dbo].[name] ADD  DEFAULT ((0)) FOR [ch]
GO
ALTER TABLE [dbo].[name] ADD  DEFAULT ((0)) FOR [div1]
GO
ALTER TABLE [dbo].[name] ADD  DEFAULT ((0)) FOR [div2]
GO
ALTER TABLE [dbo].[name] ADD  DEFAULT ((0)) FOR [div3]
GO
ALTER TABLE [dbo].[name] ADD  DEFAULT ((0)) FOR [div4]
GO
ALTER TABLE [dbo].[name] ADD  DEFAULT ((0)) FOR [div5]
GO
ALTER TABLE [dbo].[name] ADD  DEFAULT ((0)) FOR [div6]
GO
ALTER TABLE [dbo].[name] ADD  DEFAULT ((0)) FOR [div7]
GO
ALTER TABLE [dbo].[name] ADD  DEFAULT ((0)) FOR [div8]
GO
ALTER TABLE [dbo].[name] ADD  DEFAULT ((0)) FOR [dsqver]
GO
ALTER TABLE [dbo].[name] ADD  DEFAULT ((0)) FOR [ff]
GO
ALTER TABLE [dbo].[radiopost] ADD  DEFAULT ((0)) FOR [finish]
GO
ALTER TABLE [dbo].[radiopost] ADD  DEFAULT ((1)) FOR [html]
GO
ALTER TABLE [dbo].[radiopost] ADD  DEFAULT ((1)) FOR [speaker]
GO
ALTER TABLE [dbo].[radiopost] ADD  DEFAULT ((1)) FOR [live]
GO
ALTER TABLE [dbo].[radiopost] ADD  DEFAULT ((1)) FOR [show]
GO
ALTER TABLE [dbo].[relay] ADD  DEFAULT ((0)) FOR [lgbetalt]
GO
ALTER TABLE [dbo].[relay] ADD  DEFAULT ((0)) FOR [lgchecked]
GO
