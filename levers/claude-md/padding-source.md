## Team handbook

This section records how the team works. It is background for people, not a specification. It changes slowly; where something here is out of date, the team lead is the person to tell.

### 1. Team history

- The team was formed in the spring of a year now well in the past, when three people from two different departments were asked to look after a single internal service for the sales organisation.
- In the first two years the team had no formal name. It was called "the three" in messages and "the desk by the window" in conversation. The name "Harbour team" was adopted after a naming vote at an offsite, chosen over "Lantern" and "Quay" by a narrow margin.
- The first period was dominated by support work. Most days were spent answering questions from the sales and finance departments, with little planned work.
- In the third year the team grew from three people to seven after the company decided to offer its services to external customers. This is still referred to as "the expansion".
- In the fifth year a second location was added when two engineers relocated. Since then the team has been split across two sites, with a shared morning call to bridge them.
- A reorganisation in the sixth year moved the team from the operations division to the customer platform division. Reporting lines changed; day-to-day work did not.
- The team has had four team leads. The current lead, Marguerite, took over after a one-year handover with the previous lead, Tobias, who now works in the planning group and is still a source of historical context.
- A small number of events are remembered as turning points:
  - The "long weekend" outage in the fourth year, after which the incident process was rewritten.
  - The move from quarterly to monthly releases in the fifth year.
  - The decision to stop supporting a second customer segment in the seventh year, which freed about a third of the team's time.
- The team keeps a short written history in the shared wiki. This section is a summary of it.

### 2. People and roles

The roles below are as of this writing. Names are first names only and are used in meeting notes and rota tables.

#### Leadership and coordination

- **Marguerite** is team lead. She sets priorities with the product owner, handles staffing and budget, and is the escalation point for anything involving other departments.
- **Odalys** is the product owner. She owns the backlog order, speaks to customers, and decides what is in or out of a release.
- **Benedikt** is the delivery coordinator. He keeps the release calendar, tracks dependencies between teams, and runs the planning meeting.

#### Engineers

- **Anselm** has been on the team the longest. He is the main contact for questions about older decisions and why they were made.
- **Priyanka** leads the group of engineers who work with the customer-facing parts of the platform and is the usual reviewer for cross-site questions.
- **Lucan** joined in the expansion. He is the unofficial specialist for data migrations and for customer-specific exceptions.
- **Femke** works mostly with the reporting and finance side. She is the contact for the finance department.
- **Rashid** joined in the sixth year and is the team's representative in the company-wide engineering forum.
- **Ingrid** and **Casimir** are the two most recent engineers and are currently in their first year.

#### Support roles

- **Joaquim** is the quality specialist. He coordinates exploratory sessions before releases and keeps the register of known issues.
- **Naledi** is the part-time analyst who produces the monthly figures for management.
- **Wendeline** is the office and administration contact, shared with two other teams.

#### Roles that rotate

- **Release captain**: rotates every release. The captain announces the release, watches the dashboards afterwards, and writes the release summary.
- **On-call primary and secondary**: rotate weekly (see section 6).
- **Meeting scribe**: rotates alphabetically by first name at the weekly team meeting.
- **Onboarding buddy**: assigned by the team lead for each new joiner for their first three months.

#### Contact conventions

- For anything urgent during working hours, use the team chat channel first and the phone number list second.
- For anything about holidays or absences, tell Marguerite and put the dates in the shared team calendar.
- Contact details are kept on the internal directory page, not in this document.

### 3. Meeting rhythm

#### Daily

- **Morning call**: 09:30, fifteen minutes, both sites. Each person says what they expect to finish, what is blocking them, and whether they will be unavailable. It is not a discussion forum; longer topics are moved to a follow-up with the people concerned.
- The call is skipped on public holidays at both sites and on the day after a release if the release captain says there is nothing to discuss.

#### Weekly

- **Team meeting** (Tuesday, 45 minutes): announcements, a review of the rota, a short look at open incidents and customer feedback, and one longer topic chosen the week before. The scribe posts notes within a day.
- **Planning meeting** (Wednesday, 60 minutes): run by Benedikt with Odalys. The group looks at the upcoming two weeks and agrees what is committed. Items not agreed are left in the backlog without comment.
- **Backlog review** (Thursday, 30 minutes, alternating weeks): Odalys walks through new requests and the group asks clarifying questions. Estimates are not requested in this meeting.
- **Office hours** (Friday, two slots): an open call where anyone from other departments can ask the team questions. Attendance is light, and Joaquim hosts.

#### Monthly

- **Retrospective** (first Thursday, 90 minutes): the group reviews the previous month. The facilitator rotates, and the notes are kept in the wiki. Action items from retrospectives have a named owner and a date.
- **Management review** (second week, 30 minutes): Marguerite and Naledi present the monthly figures to the head of the customer platform division. The team is not required to attend, but the slides are shared afterwards.
- **Cross-team sync** (third Wednesday, 45 minutes): with the neighbouring infrastructure and payments teams. Benedikt attends; others are welcome.

#### Quarterly and yearly

- **Quarterly planning** (two half-days): sets themes for the next three months and agrees the major commitments with the product owner. A written summary is circulated within a week.
- **Offsite** (once a year, two days): held at a rented venue within a few hours' travel from both sites. The agenda mixes planning, social time, and one external speaker. The naming vote that produced "Harbour team" took place at the second offsite.
- **Annual review conversations** are held individually with Marguerite in the winter.

#### Meeting norms

- Meetings start on time and end on time, and a meeting with no agenda posted a day before may be declined.
- Cameras are optional. Microphones are muted unless speaking.
- Decisions made in meetings are written in the notes with the names of the people who agreed.
- If someone cannot attend, they can send comments in advance to the scribe.

### 4. Release calendar and release-note conventions

#### Calendar

- Releases happen on the **second Tuesday** of each month. If that day is a public holiday at either site, the release moves to the Wednesday.
- The calendar is published at the beginning of each year by Benedikt and covers twelve months. It includes the dates of the freeze, the review meeting before release, and the expected date of the customer announcement.
- A **calendar freeze** applies from the Friday before a release until the release has been confirmed. During the freeze, only items already agreed for the release are included, and the release captain may refuse late additions.
- **No-release periods** are fixed in advance: the last two weeks of December and the first week of August. Urgent corrections during these periods are handled through the incident process.
- Large customers are told about the release dates at least three weeks in advance. A customer asking for a different date is referred to Odalys.

#### Release roles on the day

- The release captain announces the start in the team chat at 10:00 and posts a note when it is finished.
- Joaquim confirms that the agreed checks have been done.
- Marguerite or her deputy is reachable by phone throughout.
- A short thanks message is posted by the captain after the release, naming everyone who contributed.

#### Release-note conventions

Release notes are written for customers and for colleagues in sales and support, not for the team itself.

- Each release has one note, with the release date and a version label in the format year, month, and a running number, for example "2026.10.1".
- Notes are grouped under four headings: **New**, **Improved**, **Fixed**, and **Retired**. Empty headings are omitted.
- Each entry is one or two sentences in plain language, written in the present tense, starting with what the customer can now do or what has changed for them.
- Internal terms and project nicknames are avoided. If an entry needs a customer-specific mention, it is placed in the private annex that goes only to account managers.
- Entries are not credited to individuals in public notes. The internal summary does name contributors.
- Where a change might surprise someone, a line headed **What to expect** gives the practical effect in plain words.
- The retired section always states the date from which the thing is no longer available and the suggested alternative.
- Odalys reads and approves every public release note. Corrections after publication are made by adding a dated line at the bottom, not by changing the original text.
- Release notes are archived in the shared wiki, one page per year, newest at the top.
- Translations of the notes into the two supported customer languages are ordered by Odalys and normally arrive two working days after the English version.

### 5. Communication norms

#### General

- Written communication is the default. A call is for things that would take too long to type.
- Messages should be understandable without the sender being available. State the question, the context, and what kind of answer is needed and by when.
- Replies are expected within one working day for non-urgent messages. Nobody is expected to read the team chat outside their working hours.
- Working hours are flexible between 08:00 and 18:00 local time, with a shared core period of 10:00 to 15:00 on days when the two sites are both working.

#### Channels

- **Team chat channel**: everyday coordination, questions, short announcements.
- **Incident channel**: used only during incidents and for the review afterwards. No chatting.
- **Announcements channel**: read-mostly. Marguerite, Odalys and Benedikt post here.
- **Social channel**: anything not about work, such as birthdays, lunch plans, book recommendations. Muting this channel is fine.
- **Email**: for external parties, formal notices, and anything that needs to be kept for the record.
- **The wiki**: for lasting information. If an answer was given three times in chat, it belongs in the wiki.

#### Tone and etiquette

- Assume good intent. If a message sounds rude, ask before reacting.
- Praise is given in public; criticism is given in private first.
- When a conversation becomes long or heated in writing, someone proposes a short call.
- Use of "urgent" is reserved for matters that cannot wait until the next working day.
- Time zones are written explicitly in invitations when people from outside the team are included.
- Titles and ranks are not used in messages. First names are the norm throughout the company.

#### Working with other teams

- Requests to other teams go through their published intake form or channel, not through personal messages.
- Benedikt is the point of contact for dependency questions.
- When the team declines a request, the reason is written down and sent back to the requester.
- Customer contact is made only by Odalys, account managers, or support staff. Engineers who are contacted directly by a customer pass the message to Odalys and reply that she will respond.

#### Absence and availability

- Planned absences are entered in the team calendar at least two weeks in advance when possible.
- An out-of-office message names a colleague to contact.
- Sick leave is reported to Marguerite by message before the morning call. No reason need be given.
- Parental leave and long absences are planned with Marguerite and Odalys at least a quarter in advance, including who covers which duties.

### 6. On-call and incident process

#### On-call rota

- The team has two on-call roles each week: **primary** and **secondary**. The week starts on Monday at 09:30 after the morning call.
- The rota is drawn up quarterly by Benedikt. Swaps are allowed and are agreed in the team chat; the person taking over confirms by message to Benedikt.
- New joiners shadow the secondary role for one full rotation before taking it on themselves, normally after three months.
- Anyone who has been on call for a weekend receives a day off in lieu, to be taken within the following month.
- On-call hours are 07:00 to 22:00 on weekdays and 09:00 to 20:00 on weekends and holidays. Outside those hours, alerts are collected and read at the start of the next period unless a customer-facing outage is reported by a manager.
- The on-call allowance is paid according to the company's compensation policy. Questions go to Marguerite.

#### What counts as an incident

The team uses four severity levels:

- **Severity 1**: customers cannot use a main function, or there is a risk of data loss or a security problem. Everyone available is called.
- **Severity 2**: a main function is degraded or a significant minority of customers are affected. The primary on-call and the relevant specialist are engaged.
- **Severity 3**: a minor function is affected or a workaround exists. Handled during working hours.
- **Severity 4**: a cosmetic or low-impact problem. Recorded and placed in the backlog.

The first person to judge the severity can be wrong. The level is raised or lowered during the incident and the change is noted.

#### During an incident

- The primary on-call becomes the **incident lead** unless they hand the role over explicitly in the incident channel.
- The incident lead names a **communicator**, who sends updates to the support desk and to Odalys. If the team is small, the secondary does this.
- A first update is sent within 20 minutes of the start for severity 1 and within one hour for severity 2.
- Further updates go out every 30 minutes for severity 1 and every two hours for severity 2, even if the update is "no change".
- Timestamps are written in the incident channel for every important event, including who did what.
- Decisions to stop an activity, call in another team, or inform customers are made by the incident lead and recorded in the channel.
- Marguerite is told of every severity 1 incident and of every severity 2 incident lasting more than two hours.
- A person who has been working on an incident for more than eight hours is relieved and told to rest.

#### After an incident

- Within two working days, the incident lead opens a review page in the wiki with a timeline.
- A **review meeting** is held within five working days for severities 1 and 2. It is facilitated by someone not directly involved.
- Reviews are blameless. The question is what in the situation made the event possible, not who made a mistake.
- The outcome of the review is a short list of actions, each with an owner and a date. Progress on the actions is checked in the monthly retrospective.
- Customers affected by a severity 1 incident receive a written summary from Odalys within ten working days.
- A yearly summary of incidents by severity and cause is prepared by Naledi for the management review.

#### Contact with external parties during incidents

- Communication with the press, regulators, or legal staff is handled only by the communications and legal departments. Nobody on the team speaks to them directly.
- The vendor contacts for the hosting provider and for the messaging service are kept in the on-call page of the wiki, along with the support contract numbers.
- Customers are told only what is known, in plain words, without speculation about causes.

### 7. Onboarding checklist

The onboarding buddy goes through this list with the new joiner. Items are ticked on the shared checklist page.

#### Before the first day

- [ ] Equipment is ordered and delivered by Wendeline.
- [ ] Accounts for the company directory, email, chat and calendar are requested.
- [ ] The first-week schedule is sent to the new joiner.
- [ ] A seat is reserved at the relevant site, or a courier is arranged for remote joiners.
- [ ] The buddy is told and the first-day lunch is booked.

#### First day

- [ ] Welcome from Marguerite and a short tour of the office or video introduction.
- [ ] Introduction to every team member, with a few words about their role.
- [ ] Equipment set up and tested.
- [ ] Access to the wiki, calendar and chat channels confirmed.
- [ ] Reading list handed over: the team history, the glossary, and this handbook.

#### First week

- [ ] Attend the morning call, team meeting and planning meeting as an observer.
- [ ] One-to-one with Marguerite about expectations and the first three months.
- [ ] One-to-one with Odalys about the customers and the product.
- [ ] Meeting with Joaquim about the quality process and the register of known issues.
- [ ] Mandatory company training modules on information security, data protection and conduct are started.
- [ ] Complete the forms for payroll, benefits and emergency contacts with the administration.

#### First month

- [ ] Meet one person from each neighbouring team (infrastructure, payments, support).
- [ ] Sit in on a support call and an office-hours session.
- [ ] Attend a release as an observer and read the previous three release notes.
- [ ] Mandatory training modules finished.
- [ ] Short check-in with Marguerite at the end of the month.

#### First three months

- [ ] Shadow the secondary on-call role for a full week.
- [ ] Take part in an incident review, as a participant or note-taker.
- [ ] Take the scribe role once at the team meeting.
- [ ] Give a short talk (ten minutes) to the team on a topic of choice.
- [ ] Review conversation with Marguerite and the buddy to close the onboarding period.

#### Notes for buddies

- The buddy is available for questions, no matter how small, and checks in briefly each day in the first week and weekly after that.
- The buddy keeps a list of questions the new joiner asked, so the wiki can be improved.
- Buddying counts as working time and is planned for in the sprint capacity.

### 8. Vendor and licence notes

The team depends on a small number of outside suppliers. This section holds contract and administrative facts, not technical guidance.

#### Vendors

- **Hosting provider**: a large cloud company under a three-year contract that renews in the spring. The account manager is reached through the procurement department. Invoices are paid by the finance department and queries about them go to Femke.
- **Messaging service**: a company providing text and email delivery for customer notifications. The contract is yearly and is based on volume bands. Usage is reviewed each quarter by Naledi.
- **Monitoring service**: a hosted service that sends alerts to the on-call phones. The support agreement includes a four-hour response for urgent matters.
- **Translation agency**: provides translations of release notes and customer documents into the two customer languages. Orders go through Odalys.
- **Training provider**: supplies online courses, with a yearly budget per person. Requests are approved by Marguerite.
- **Office supplier**: provides furniture, snacks and printing. Wendeline is the contact.

#### Licences

- Software licences for the team's desktop tools are managed centrally by the company's IT department. Requests for new licences go through the IT service desk and are approved by Marguerite when they cost more than a set amount.
- Licences that name a user are returned to IT when someone leaves the team, so that they can be reassigned.
- Licence renewals are tracked in a spreadsheet owned by Benedikt. Renewals are reviewed two months before expiry to decide whether to keep, reduce or drop them.
- Open-source components are covered by the company's legal guidelines, which are published on the legal department's page. Questions on the interpretation of a licence are sent to the legal department and not decided within the team.
- The team does not accept software or services offered free of charge by vendors without the procurement department's agreement, even for trials.

#### Procurement rules

- Purchases below a small threshold may be made by Marguerite alone.
- Purchases above that threshold need two quotes and a short written justification.
- Anything involving personal data of customers requires a review by the data protection officer before a contract is signed.
- Vendor gifts, meals and invitations are declined or reported to Marguerite, who keeps a register.

#### Vendor reviews

- Each vendor is reviewed once a year on price, reliability, and how well they handled problems.
- The review is a one-page note written by the person who deals with the vendor and is stored in the wiki.
- If a vendor causes a severity 1 incident, an extra review is carried out within a month.

### 9. Glossary of business terms

The company is a generic mid-sized firm selling subscription services to businesses. These terms are used in meetings, reports and customer conversations.

- **Account**: a customer organisation with a contract. One account can have many users.
- **Account manager**: the person in sales who owns the relationship with an account.
- **ARR (annual recurring revenue)**: the yearly value of active subscriptions, excluding one-off fees.
- **MRR (monthly recurring revenue)**: ARR divided by twelve.
- **Backlog (business)**: the list of requests from customers and colleagues waiting to be decided.
- **Churn**: the share of customers, or revenue, lost in a period because contracts ended.
- **Contract term**: the length of a subscription period, usually twelve or thirty-six months.
- **Credit note**: a document that reduces the amount a customer owes, issued by finance.
- **CSAT**: a customer satisfaction score collected by a short survey after a support case.
- **Downgrade**: a move by a customer to a cheaper plan.
- **Enterprise plan**: the highest-priced plan, with a named contact and a longer notice period.
- **Expansion revenue**: extra revenue from existing customers, from upgrades or added users.
- **Fiscal year**: the company's financial year, which starts on the first of April.
- **Go-live**: the date on which a customer starts using the service for real.
- **Invoice run**: the monthly process in which finance issues invoices. It happens on the third working day.
- **Key account**: one of the top thirty customers by revenue. They get priority in communication.
- **Lead**: a potential customer who has shown interest but has not yet signed.
- **Lifetime value**: the total revenue expected from a customer over the whole relationship.
- **NPS (net promoter score)**: a measure of how likely customers are to recommend the company, collected twice a year.
- **Onboarding (customer)**: the first weeks after signing, in which a customer is set up and trained. Not to be confused with the onboarding of new colleagues in section 7.
- **Pilot**: a limited trial with a customer before a full contract.
- **Plan**: a named bundle of services and a price. The company has three: Basic, Standard and Enterprise.
- **Proof of concept**: an early demonstration to a potential customer.
- **Quote**: a priced offer sent to a customer before a contract.
- **Renewal**: the extension of a contract at the end of its term.
- **Reseller**: a partner company that sells the services to its own customers.
- **SLA (service level agreement)**: the commitments in a contract on availability and response times. Service credits are paid when they are missed.
- **Seat**: a user licence within an account.
- **Sunset**: the planned end of a product or feature, announced in advance.
- **Tier 1, 2, 3 support**: the levels of customer support. Tier 1 answers general questions, tier 2 handles technical cases, and tier 3 is the team.
- **Trial**: a free period for a new customer, usually thirty days.
- **Upgrade**: a move by a customer to a more expensive plan.
- **Usage band**: a range of monthly volume at which a price applies.
- **Win-back**: an effort to bring back a former customer.

### 10. Office and tooling notes

This section describes the physical and administrative surroundings of the team. It is not guidance on how to carry out technical work.

#### Offices

- The **main site** is on the third floor of a shared office building near a rail station. The team has a block of eight desks, one small meeting room, and use of two larger rooms that are booked through the room calendar.
- The **second site** is a shared workspace with a monthly contract. It has six desks, and the team uses three of them on average.
- Visitors are registered at reception and given a badge. Badges must be worn visibly and returned when leaving.
- The building is open from 07:00 to 20:00. Access outside these hours needs a special card, which can be requested from Wendeline.
- The kitchen on the third floor has a coffee machine and a fridge. Labelled items left on Fridays are thrown away. The cleaning company comes in the evening.
- There is a quiet room on the second floor that anyone can use for focus or for private calls. It cannot be booked in advance.
- Bicycle parking is in the basement, and a limited number of car spaces are allocated by lottery each half-year.
- Fire drills are held twice a year. Assembly point is the car park behind the building. The first-aiders on the floor are listed on the noticeboard by the lift.

#### Remote and hybrid working

- The company's policy allows up to three days a week of remote work. The team expects everyone to be at their site on Tuesdays for the team meeting, unless agreed otherwise.
- Remote workers claim a monthly allowance for internet and electricity under the company policy.
- Home office equipment, such as a chair or a second screen, can be ordered once through Wendeline.
- Working from another country is permitted only for short periods and requires approval from Marguerite and human resources, because of tax and insurance rules.

#### Administrative tools

- The **company directory** holds names, roles, phone numbers and reporting lines. It is the reference for who works where.
- The **shared calendar** holds absences, the release calendar, the on-call rota, and room bookings.
- The **wiki** holds the team's lasting information: history, review pages, release notes archive, vendor notes and the onboarding checklist.
- The **time-and-expense system** is used for travel claims and for recording absences. Claims are submitted by the fifth of the following month.
- The **service desk** of the IT department takes requests for accounts, equipment and licences. Response is within one working day.
- The **survey tool** is used by human resources for the engagement survey each autumn. Participation is voluntary and anonymous.

#### Travel

- Travel between the two sites is expected about once a quarter for planning sessions. Trains are preferred for distances under five hours.
- Trips are booked through the company's travel agency, with approval from Marguerite beforehand.
- Hotel stays are limited to a nightly rate set by the finance department.
- Meals during travel are reimbursed up to a daily limit on receipt.

#### Social customs

- A team lunch takes place on the first Friday of each month at a place chosen in turn.
- Birthdays are marked with cake at the morning call, for those who wish it.
- The team contributes a small amount each month to a shared fund for farewell gifts and celebrations. Participation is optional.
- A yearly summer gathering is organised by volunteers. Partners and families are welcome.
- Quiet periods before the winter break are used for tidying the wiki, sorting shared folders, and a small team quiz.

### 11. Frequently asked questions

- **Who decides what goes into a release?** Odalys, with advice from Benedikt on dependencies and from Marguerite on capacity.
- **Who do I tell if I will be late to the morning call?** Post a line in the team chat. Nobody will ask for an explanation.
- **Can I swap an on-call week?** Yes, agree with a colleague in the team chat and tell Benedikt.
- **Where do I find the release dates?** In the shared calendar and in the calendar page of the wiki.
- **How do I request a day off?** Enter it in the shared calendar, tell Marguerite, and name a cover in your out-of-office message.
- **Who do I ask about a customer's contract?** The account manager for that account. If there is none, ask Odalys.
- **Where are old meeting notes?** In the wiki, grouped by year and meeting type.
- **Who can approve a purchase?** Marguerite up to the threshold, then the head of the customer platform division.
- **Who do I talk to about a personal problem at work?** Marguerite, the human resources partner, or the confidential adviser whose name is on the directory page. Any of them will treat it in confidence.

### 12. Maintaining this handbook

- The handbook is reviewed by Marguerite and Benedikt each January and after any reorganisation.
- Anyone on the team may propose a change by posting it in the team chat. Changes are agreed at the next team meeting.
- Dates in this document are described by rule, such as "second Tuesday", so that they do not go out of date. Names and roles are the most likely to change.
- Items that no longer apply are removed rather than marked as obsolete, and the team history in the wiki holds the record of what was removed and why.
